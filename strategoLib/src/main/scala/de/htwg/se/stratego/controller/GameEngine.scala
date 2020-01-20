package de.htwg.se.stratego.controller

import com.google.inject.{ Guice, Injector }
import play.api.libs.json
import de.htwg.se.stratego.StrategoModule
import de.htwg.se.stratego.model._
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.stratego.model.boardComponent.{ Coordinates, Field, Figure, GameBoardInterface }
import de.htwg.se.stratego.model.fileIoComponent.FileIOInterface
import de.htwg.se.stratego.view.StrategoTUI

import scala.swing.Publisher
import scala.swing.event.Event

class GameEngine extends Subject[GameEngine] with Publisher {
  val injector: Injector = Guice.createInjector(new StrategoModule)
  var gb: GameBoardInterface = injector.instance[GameBoardInterface]

  def exit(): Unit = {
    System.exit(0)
  }

  def move(from: Coordinates, to: Coordinates): Boolean = {
    // No figure on field "from" OR field locked
    if (gb.get(from).isEmpty || gb.get(to).isLocked) {
      return false
    }

    val attacker = gb.get(from).fig.get

    if (!canMove(from, to) || !attacker.isMovable || attacker.player != gb.currentPlayer) {
      return false
    }

    if (gb.get(to).isEmpty) {
      gb.move(from, to)
      switchPlayers()
      publish(new CellChanged)
      return true
    }

    val defender = gb.get(to).fig.get

    // Attacker must be the current player, cannot beat own figures
    if (defender.player == gb.currentPlayer) {
      return false
    }

    var attackerWins = false
    var defenderWins = false

    if (attacker.strength == Figure.MINER && defender.strength == Figure.BOMB) {
      // Miner defuses Bomb
      attackerWins = true
    } else if (attacker.strength == Figure.SPY && defender.strength == Figure.MARSHAL) {
      // Spy kills Marshal
      attackerWins = true
    } else if (defender.strength == Figure.FLAG) {
      // Attacker wins game
      attackerWins = true
      println("\n" + attacker.player.name + " wins")
      exit()
    } else if (attacker.strength > defender.strength) {
      // Attacker wins
      attackerWins = true
    } else if (defender.strength > attacker.strength) {
      // Defender wins
      defenderWins = true
    } else if (defender.strength == attacker.strength) {
      // Figures have same strength
    }

    if (attackerWins) {
      gb.move(from, to)
    } else if (defenderWins) {
      gb.set(from, None)
    } else {
      gb.set(from, None)
      gb.set(to, None)
    }
    switchPlayers()
    publish(new CellChanged)
    true
  }

  def canMove(from: Coordinates, to: Coordinates): Boolean = {
    val fig = gb.get(from).fig
    if (fig.isEmpty) {
      false
    } else if (gb.get(to).fig.isDefined && gb.get(from).fig.get.player == gb.get(to).fig.get.player) {
      false
    } else if (fig.get.strength == Figure.SCOUT) {
      canMoveScout(from, to)
    } else if (!fig.get.isMovable) {
      false
    } else {
      canMoveDefaultFigure(from, to)
    }
  }

  def canMoveDefaultFigure(from: Coordinates, to: Coordinates): Boolean = {
    val differenceX = Math.abs(from.x - to.x)
    val differenceY = Math.abs(from.y - to.y)
    differenceX + differenceY == 1 && !gb.get(to).isLocked
  }

  def canMoveScout(from: Coordinates, to: Coordinates): Boolean = {
    !gb.get(to).isLocked && !movesDiagonally(from, to) && !isFigureInBetween(from, to)
  }

  def movesDiagonally(from: Coordinates, to: Coordinates): Boolean = {
    from.x != to.x && from.y != to.y
  }

  def isFigureInBetween(from: Coordinates, to: Coordinates): Boolean = {
    var figureAmount = 0
    val xstep = if (from.x < to.x) 1 else -1
    for (i <- from.x + xstep until to.x by xstep) {
      if (!gb.get(Coordinates(i, from.y)).isEmpty || gb.get(Coordinates(i, from.y)).isLocked) {
        figureAmount += 1
      }
    }
    val ystep = if (from.y < to.y) 1 else -1
    for (i <- from.y + ystep until to.y by ystep) {
      if (!gb.get(Coordinates(from.x, i)).isEmpty || gb.get(Coordinates(from.x, i)).isLocked) {
        figureAmount += 1
      }
    }
    figureAmount > 0
  }

  def switchPlayers(): Unit = {
    if (gb.currentPlayer == gb.playerOne) {
      gb.currentPlayer = gb.playerTwo
    } else if (gb.currentPlayer == gb.playerTwo) {
      gb.currentPlayer = gb.playerOne
    }
    notifyObservers()
    publish(new CellChanged)
  }

  def unset(coord: Coordinates): Boolean = {
    val field = gb.get(coord)
    if (!field.isEmpty && !field.isLocked) {
      gb.currentPlayer.remainingFigures(field.fig.get.strength) += 1
      gb.set(coord, None)
      notifyObservers()
      publish(new CellChanged)
      true
    } else {
      false
    }
  }

  def get(coord: Coordinates): Field = {
    gb.get(coord)
  }

  def set(coord: Coordinates): Boolean = {
    if (!canSet(coord)) {
      false
    } else {
      gb.set(coord, gb.currentPlayer.selectedFigure)
      gb.currentPlayer.placedFigure()
      notifyObservers()
      publish(new CellChanged)
      if (!gb.currentPlayer.hasUnplacedFigures) {
        switchPlayers()
      }
      true
    }
  }

  def canSet(coord: Coordinates): Boolean = {
    if (coord.x > gb.size || coord.x < 1 || coord.y > gb.size || coord.y < 1) {
      false
    } else if (gb.currentPlayer.selectedFigure == null) {
      false
    } else if (gb.currentPlayer == gb.playerOne && coord.y < 7) {
      false
    } else if (gb.currentPlayer == gb.playerTwo && coord.y > 4) {
      false
    } else if (gb.get(coord).isLocked || !gb.get(coord).isEmpty) {
      //Blocked fields
      false
    } else {
      true
    }
  }

  def selectFigure(strength: Int): Boolean = {
    val player = gb.currentPlayer
    if (strength <= Figure.BOMB && strength >= Figure.FLAG && player.remainingFigures(strength) != 0) {
      player.selectedFigure = Some(Figure.withStrength(player, strength))
      notifyObservers()
      true
    } else {
      false
    }
  }

  def newGame(): Unit = {
    gb = injector.instance[GameBoardInterface]
    notifyObservers()
    publish(new CellChanged)
  }

  def loadGame(): Unit = {
    gb = injector.instance[FileIOInterface].load.get
    notifyObservers()
    publish(new CellChanged)
  }

  def saveGame(): Unit = {
    injector.instance[FileIOInterface].save(gb)
  }
  def toJson(): json.JsObject = {
    injector.instance[FileIOInterface].repr(gb)
  }
}

object GameEngine {
  var engine = new GameEngine()

  def main(args: Array[String]): Unit = {
  }
}

class CellChanged extends Event