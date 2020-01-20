package de.htwg.se.stratego.model.fileIoComponent.fileIoJsonImpl

import de.htwg.se.stratego.model.boardComponent.{Coordinates, Figure, GameBoard, GameBoardInterface}
import de.htwg.se.stratego.model.fileIoComponent.FileIOInterface
import play.api.libs.json._

import scala.io.Source

class FileIO extends FileIOInterface {

  override def load: Option[GameBoardInterface] = {
    val grid: GameBoardInterface = new GameBoard()
    val source: String = Source.fromFile("grid.json").getLines.mkString
    val file: JsValue = Json.parse(source)
    val currentPlayer = file \ "currentPlayer"

    val playerOne = file \ "playerOne" \ "remaining" \\ "figure"
    val playerOneSelected = (file \ "playerOne" \ "selected").as[Int]
    if (playerOneSelected != -1) {
      grid.playerOne.selectedFigure = Some(Figure.withStrength(grid.playerOne, playerOneSelected))
    }
    for (fig <- playerOne) {
      val strength: Int = (fig \ "strength").as[Int]
      val count: Int = (fig \ "remaining").as[Int]
      grid.playerOne.remainingFigures.put(strength, count)
    }

    val playerTwo = file \ "playerTwo" \ "remaining" \\ "figure"
    val playerTwoSelected = (file \ "playerTwo" \ "selected").as[Int]
    if (playerTwoSelected != -1) {
      grid.playerTwo.selectedFigure = Some(Figure.withStrength(grid.playerTwo, playerTwoSelected))
    }
    for (fig <- playerTwo) {
      val strength: Int = (fig \ "strength").as[Int]
      val count: Int = (fig \ "remaining").as[Int]
      grid.playerTwo.remainingFigures.put(strength, count)
    }

    grid.currentPlayer = if (currentPlayer.as[Boolean]) grid.playerOne else grid.playerTwo

    val cellNodes= file \ "cells" \\ "cell"
    for (cell <- cellNodes) {
      val row: Int = (cell \ "row").as[Int]
      val col: Int = (cell \ "col").as[Int]
      val strength: Int = (cell \ "strength").as[Int]
      val isFirst: Boolean = (cell \ "isFirst").as[Boolean]
      val player = if (isFirst) grid.playerOne else grid.playerTwo
      grid.set(Coordinates(row,col), Some(Figure.withStrength(player, strength)))
    }
    Some(grid)
  }

  override def save(grid: GameBoardInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("grid.json"))
    pw.write(Json.prettyPrint(gridToJson(grid)))
    pw.close()
  }

  def gridToJson(grid: GameBoardInterface): JsObject = {

    var selectedOne: Int = -1
    var selectedTwo: Int = -1
    if (grid.playerOne.selectedFigure.isDefined) {
      selectedOne = grid.playerOne.selectedFigure.get.strength
    }
    if (grid.playerTwo.selectedFigure.isDefined) {
      selectedTwo = grid.playerTwo.selectedFigure.get.strength
    }

    Json.obj(
      "currentPlayer" -> JsBoolean(grid.playerOne == grid.currentPlayer),
      "playerOne" -> Json.obj(
        "name" -> JsString(grid.playerOne.name.toString),
        "selected" -> JsNumber(selectedOne),
        "remaining" -> Json.toJson(
          for {
            (str, rem) <- grid.playerOne.remainingFigures
          } yield figureToJson(str, rem)
        )
      ),
      "playerTwo" -> Json.obj(
        "name" -> JsString(grid.playerTwo.name.toString),
        "selected" -> JsNumber(selectedTwo),
        "remaining" -> Json.toJson(
          for {
            (str, rem) <- grid.playerTwo.remainingFigures
          } yield figureToJson(str, rem)
        )
      ),
      "cells" -> JsArray(
        for {
          row <- 1 to grid.size
          col <- 1 to grid.size
        } yield cellToJson(grid, Coordinates(row,col))
      ),
    )
  }

  def cellToJson(grid:GameBoardInterface, coordinates: Coordinates):JsObject = {
    if (grid.get(coordinates).fig.isDefined) {
      val isFirst:Boolean = grid.get(coordinates).fig.get.player == grid.playerOne
      return Json.obj(
        "cell" -> Json.obj(
          "row" -> JsNumber(coordinates.x),
          "col" -> JsNumber(coordinates.y),
          "isFirst" -> JsBoolean(isFirst),
          "strength" -> JsNumber(grid.get(coordinates).fig.get.strength)
        )
      )
    }
    Json.obj()
  }

  def figureToJson(strength:Int, remaining:Int): JsObject = {
    Json.obj(
      "figure" -> Json.obj(
        "strength" -> JsNumber(strength),
        "remaining" -> JsNumber(remaining)
      )
    )
  }

  override def repr(grid: GameBoardInterface): JsObject = {
    gridToJson(grid)
  }

}
