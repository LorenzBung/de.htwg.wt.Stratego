package controllers

import de.htwg.se.stratego.controller.GameEngine
import javax.inject._
import play.api.mvc._

@Singleton
class StrategoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val eng = GameEngine.engine
  def stratego = Action {
    Ok(views.html.stratego(eng))
  }
  def newGame = Action {
    GameEngine.engine.newGame()
    Ok(views.html.stratego(eng))
  }
  def set(x: Int, y: Int) = Action {
    val coords = de.htwg.se.stratego.model.boardComponent.Coordinates(x, y)
    eng.set(coords)
    Ok(eng.get(coords).toString())
  }
  def unset(x: Int, y: Int) = Action {
    val coords = de.htwg.se.stratego.model.boardComponent.Coordinates(x, y)
    eng.unset(coords)
    Ok(eng.get(coords).toString())
  }
  def select(strength: Int) = Action {
    eng.selectFigure(strength)
    Ok(views.html.stratego(eng))
  }
  def move(x1: Int, y1: Int, x2: Int, y2: Int) = Action {
    val from = de.htwg.se.stratego.model.boardComponent.Coordinates(x1, y1)
    val to = de.htwg.se.stratego.model.boardComponent.Coordinates(x2, y2)
    eng.move(from, to)
    Ok(views.html.stratego(eng))
  }
  def about = Action {
    Ok(views.html.about())
  }
  def getJson = Action {
    Ok(de.htwg.se.stratego.controller.GameEngine.engine.toJson())
  }
}