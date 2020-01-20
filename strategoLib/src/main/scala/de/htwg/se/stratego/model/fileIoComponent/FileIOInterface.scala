package de.htwg.se.stratego.model.fileIoComponent

import de.htwg.se.stratego.model.boardComponent.GameBoardInterface
import play.api.libs.json.JsObject

trait FileIOInterface {

  def load: Option[GameBoardInterface]
  def save(grid: GameBoardInterface): Unit
  def repr(grid: GameBoardInterface): JsObject

}
