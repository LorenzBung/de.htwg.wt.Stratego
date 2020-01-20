package de.htwg.se.stratego.model.fileIoComponent.fileIoXmlImpl

import de.htwg.se.stratego.model.boardComponent.{ Coordinates, Figure, GameBoard, GameBoardInterface }
import de.htwg.se.stratego.model.fileIoComponent.FileIOInterface
import play.api.libs.json.JsObject

import scala.xml.{ Elem, XML }

class FileIO extends FileIOInterface {

  override def load: Option[GameBoardInterface] = {
    val grid: GameBoardInterface = new GameBoard()
    val file = XML.loadFile("grid.xml")

    val currentPlayer = file \\ "currentPlayer"

    val playerOne = file \\ "playerOne" \ "remaining" \ "figure"
    val playerOneSelected = (file \\ "playerOne" \ "@selected").text.toInt
    if (playerOneSelected != -1) {
      grid.playerOne.selectedFigure = Some(Figure.withStrength(grid.playerOne, playerOneSelected))
    }
    for (fig <- playerOne) {
      val strength: Int = (fig \ "@strength").text.toInt
      val count: Int = fig.text.trim.toInt
      grid.playerOne.remainingFigures.put(strength, count)
    }

    val playerTwo = file \\ "playerTwo" \ "remaining" \ "figure"
    val playerTwoSelected = (file \\ "playerTwo" \ "@selected").text.toInt
    if (playerTwoSelected != -1) {
      grid.playerTwo.selectedFigure = Some(Figure.withStrength(grid.playerTwo, playerTwoSelected))
    }
    for (fig <- playerTwo) {
      val strength: Int = (fig \ "@strength").text.toInt
      val count: Int = fig.text.trim.toInt
      grid.playerTwo.remainingFigures.put(strength, count)
    }

    grid.currentPlayer = if (currentPlayer.text.toBoolean) grid.playerOne else grid.playerTwo

    val cellNodes = file \\ "cell"
    for (cell <- cellNodes) {
      val row: Int = (cell \ "@row").text.toInt
      val col: Int = (cell \ "@col").text.toInt
      val strength: Int = cell.text.trim.toInt
      val isFirst: Boolean = (cell \ "@isFirst").text.toBoolean
      val player = if (isFirst) grid.playerOne else grid.playerTwo
      grid.set(Coordinates(row, col), Some(Figure.withStrength(player, strength)))
    }
    Some(grid)
  }

  def save(grid: GameBoardInterface): Unit = {
    XML.save("grid.xml", gridToXml(grid))
  }

  def gridToXml(grid: GameBoardInterface): Elem = {
    var selectedOne: Int = -1
    var selectedTwo: Int = -1
    if (grid.playerOne.selectedFigure.isDefined) {
      selectedOne = grid.playerOne.selectedFigure.get.strength
    }
    if (grid.playerTwo.selectedFigure.isDefined) {
      selectedTwo = grid.playerTwo.selectedFigure.get.strength
    }

    <grid>
      <currentPlayer>{ (grid.playerOne == grid.currentPlayer).toString }</currentPlayer>
      <playerOne name={ grid.playerOne.name.toString } selected={ selectedOne.toString }>
        <remaining>
          {
            for {
              (k, v) <- grid.playerOne.remainingFigures
            } yield figureToXml(k, v)
          }
        </remaining>
      </playerOne>
      <playerTwo name={ grid.playerTwo.name.toString } selected={ selectedTwo.toString }>
        <remaining>
          {
            for {
              (k, v) <- grid.playerTwo.remainingFigures
            } yield figureToXml(k, v)
          }
        </remaining>
      </playerTwo>
      {
        for {
          row <- 1 to grid.size
          col <- 1 to grid.size
        } yield cellToXml(grid, Coordinates(row, col))
      }
    </grid>
  }

  def cellToXml(grid: GameBoardInterface, coordinates: Coordinates): Any = {
    if (grid.get(coordinates).fig.isDefined) {
      val isFirst: Boolean = grid.get(coordinates).fig.get.player == grid.playerOne
      <cell row={ coordinates.x.toString } col={ coordinates.y.toString } isFirst={ isFirst.toString }>
        { grid.get(coordinates).fig.get.strength.toString }
      </cell>
    }
  }

  def figureToXml(k: Int, v: Int): Elem = {
    <figure strength={ k.toString }>
      { v.toString }
    </figure>
  }

  override def repr(grid: GameBoardInterface): JsObject = {
    null
  }
}
