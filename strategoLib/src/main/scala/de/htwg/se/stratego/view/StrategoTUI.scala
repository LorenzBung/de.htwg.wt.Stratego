package de.htwg.se.stratego.view

import de.htwg.se.stratego.controller.GameEngine
import de.htwg.se.stratego.model._
import de.htwg.se.stratego.model.boardComponent.{ Coordinates, GameBoardInterface }

import scala.io.StdIn

class StrategoTUI extends Observer[GameEngine] {
  var engine: GameEngine = GameEngine.engine

  def start() {
    println()
    println("""  _______/  |_____________ _/  |_  ____   ____   ____  """)
    println(""" /  ___/\   __\_  __ \__  \\   __\/ __ \ / ___\ /  _ \ """)
    println(""" \___ \  |  |  |  | \// __ \|  | \  ___// /_/  >  <_> )""")
    println("""/____  > |__|  |__|  (____  /__|  \___  >___  / \____/ """)
    println("""     \/                   \/          \/_____/         """)
    println()

    println("Available Figures:")
    println("0: Flag")
    println("1: Spy")
    println("2: Scout")
    println("3: Miner")
    println("4: Sergeant")
    println("5: Lieutenant")
    println("6: Captain")
    println("7: Major")
    println("8: Colonel")
    println("9: General")
    println("10: Marshal")
    println("11: Bomb")
    println()

    engine.addObserver(this)

    mainLoop()
  }

  def printPrompt(player: Player): Unit = {
    var playerColor = if (engine.gb.currentPlayer == engine.gb.playerOne) Console.RED else Console.BLUE
    if (player.selectedFigure.isDefined) {
      print(playerColor + player.name + Console.RESET + " " + player.selectedFigure.get.description + " (" + player.remainingFigures(player.selectedFigure.get.strength) + " left)> " + Console.RESET)
    } else {
      print(playerColor + player.name + Console.RESET + "> ")
    }
  }

  def mainLoop(): Unit = {
    printPrompt(engine.gb.currentPlayer)
    var line: String = ""
    while (!{ line = StdIn.readLine(); line }.isEmpty) {
      val params = line.split(" ")

      params match {
        case Array("set", x, y) if x.forall(_.isDigit) && y.forall(_.isDigit) =>
          if (engine.gb.currentPlayer.selectedFigure.isDefined) {
            if (engine.set(Coordinates(x.toInt, y.toInt))) {
              print("Placed")
            } else {
              println(Console.RED + "Couldn't place " + engine.gb.currentPlayer.selectedFigure.get.description + Console.RESET)
              printPrompt(engine.gb.currentPlayer)
            }
          } else {
            println("No Figure selected")
            printPrompt(engine.gb.currentPlayer)
          }

        case Array("select", fig) if fig.forall(_.isDigit) =>
          val strength = fig.toInt
          if (engine.selectFigure(strength)) {
            print("Selected")
          } else {
            println("You don't have any Figures of this strength")
            printPrompt(engine.gb.currentPlayer)
          }
        case Array("show", _*) =>
          println(engine.gb)

        case Array("move", a, b, c, d) =>
          val x1 = a.toInt
          val y1 = b.toInt
          val x2 = c.toInt
          val y2 = d.toInt
          engine.move(Coordinates(x1, y1), Coordinates(x2, y2))

        case Array("exit", _*) =>
          engine.exit()

        case _ =>
          println("Available Commands:")
          println("show, select <STRENGTH>, set <X> <Y>, move <X1> <Y1> <X2> <Y2>, exit")
          printPrompt(engine.gb.currentPlayer)
      }
    }
  }

  override def receiveUpdate(subject: GameEngine): Unit = {
    var player = subject.gb.currentPlayer
    println()
    printPrompt(player)
  }
}
