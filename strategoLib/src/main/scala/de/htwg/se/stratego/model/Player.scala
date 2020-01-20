package de.htwg.se.stratego.model

import de.htwg.se.stratego.model.boardComponent.Figure

case class Player(name: String) {
  val remainingFigures = scala.collection.mutable.Map(
    Figure.FLAG -> 1,
    Figure.MARSHAL -> 1,
    Figure.GENERAL -> 1,
    Figure.COLONEL -> 2,
    Figure.MAJOR -> 3,
    Figure.CAPTAIN -> 4,
    Figure.LIEUTENANT -> 4,
    Figure.SERGEANT -> 4,
    Figure.MINER -> 5,
    Figure.SCOUT -> 8,
    Figure.SPY -> 1,
    Figure.BOMB -> 6)

  override def toString: String = name
  var figures: List[Figure] = List[Figure]()
  var selectedFigure: Option[Figure] = None

  def placedFigure(): Unit = {
    if (selectedFigure.isDefined) {
      this.remainingFigures(this.selectedFigure.get.strength) -= 1

      if (this.remainingFigures(this.selectedFigure.get.strength) == 0) {
        this.selectedFigure = None
      }
    }
  }

  def hasUnplacedFigures: Boolean = {
    var hasUnplacedFigures = false
    for ((_, v) <- this.remainingFigures) {
      if (v != 0) hasUnplacedFigures = true
    }
    hasUnplacedFigures
  }
}

