package de.htwg.se.stratego.model.boardComponent

import de.htwg.se.stratego.model.Player

abstract case class Figure(player: Player, strength: Int, description: String) {
  require(strength <= 11 && strength >= 0, "strength must be between 0 and 11")
  val movable: Boolean = true

  def isMovable: Boolean = {
    this.movable
  }

  override def toString: String = {
    strength.toString
  }
}

object Figure {
  class Spy(p: Player) extends Figure(p, SPY, "Spy")
  class Scout(p: Player) extends Figure(p, SCOUT, "Scout")
  class Miner(p: Player) extends Figure(p, MINER, "Miner")
  class Sergeant(p: Player) extends Figure(p, SERGEANT, "Sergeant")
  class Lieutenant(p: Player) extends Figure(p, LIEUTENANT, "Lieutenant")
  class Captain(p: Player) extends Figure(p, CAPTAIN, "Captain")
  class Major(p: Player) extends Figure(p, MAJOR, "Major")
  class Colonel(p: Player) extends Figure(p, COLONEL, "Colonel")
  class General(p: Player) extends Figure(p, GENERAL, "General")
  class Marshal(p: Player) extends Figure(p, MARSHAL, "Marshal")
  class Flag(p: Player) extends Figure(p, FLAG, "Flag") {
    override val movable: Boolean = false
    override def toString: String = {
      "F"
    }
  }
  class Bomb(p: Player) extends Figure(p, BOMB, "Bomb") {
    override val movable: Boolean = false
    override def toString: String = {
      "B"
    }
  }

  val FLAG = 0
  val SPY = 1
  val SCOUT = 2
  val MINER = 3
  val SERGEANT = 4
  val LIEUTENANT = 5
  val CAPTAIN = 6
  val MAJOR = 7
  val COLONEL = 8
  val GENERAL = 9
  val MARSHAL = 10
  val BOMB = 11

  def withStrength(player: Player, strength: Int): Figure = {
    strength match {
      case FLAG => new Figure.Flag(player)
      case SPY => new Figure.Spy(player)
      case SCOUT => new Figure.Scout(player)
      case MINER => new Figure.Miner(player)
      case SERGEANT => new Figure.Sergeant(player)
      case LIEUTENANT => new Figure.Lieutenant(player)
      case CAPTAIN => new Figure.Captain(player)
      case MAJOR => new Figure.Major(player)
      case COLONEL => new Figure.Colonel(player)
      case GENERAL => new Figure.General(player)
      case MARSHAL => new Figure.Marshal(player)
      case BOMB => new Figure.Bomb(player)
    }
  }
}
