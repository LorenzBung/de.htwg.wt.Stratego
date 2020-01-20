package de.htwg.se.stratego.model.boardComponent

class Field {

  var fig: Option[Figure] = None
  var locked: Boolean = false

  def this(fig: Figure) {
    this()
    this.setFigure(Some(fig))
  }

  def isEmpty: Boolean = {
    fig.isEmpty
  }

  def isLocked: Boolean = {
    locked
  }

  def lock(): Unit = {
    locked = true
  }

  def setFigure(newFig: Option[Figure]): Unit = {
    fig = newFig
  }

  override def toString: String = {
    if (isLocked) {
      "X"
    } else if (isEmpty) {
      " "
    } else {
      fig.getOrElse("Err").toString
    }

  }
}
