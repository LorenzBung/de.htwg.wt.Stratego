package de.htwg.se.stratego.model.boardComponent

import de.htwg.se.stratego.model._

class GameBoard(fields: Array[Array[Field]]) extends GameBoardInterface {
  var playerOne = Player("Player 1")
  var playerTwo = Player("Player 2")
  var currentPlayer: Player = playerOne
  final val size = GameBoard.size

  def set(coords: Coordinates, figure: Option[Figure]): Unit = {
    fields(coords.y - 1)(coords.x - 1).setFigure(figure)
  }

  def this() {
    this(Array.tabulate(GameBoard.size, GameBoard.size)((x, y) => {
      val field = new Field()

      if (x == 4 || x == 5) y match {
        case 2 | 3 | 6 | 7 => field.lock()
        case _ =>
      }

      field
    }))
  }

  def get(coords: Coordinates): Field = {
    fields(coords.y - 1)(coords.x - 1)
  }

  def move(from: Coordinates, to: Coordinates): Unit = {
    set(to, get(from).fig)
    set(from, None)
  }

  def matrix: Array[Array[Field]] = {
    fields
  }

  override def toString: String = {
    var board = "   #"

    var i = 1

    for (i <- 1 to 10) {
      board += " %1$2d  |".format(i)
    }

    board += "\n"

    for (x <- 0 to 9) {
      board += "%1$2d # ".format(x + 1)
      for (y <- 0 to 9) {
        val f = fields(x)(y)
        board += "%1$2s ".format(f) + " | "
      }
      board += "\n"
    }

    board
  }
}

object GameBoard {
  final val size = 10
}