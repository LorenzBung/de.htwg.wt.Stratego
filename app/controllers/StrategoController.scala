package controllers

import de.htwg.se.stratego.controller.GameEngine
import javax.inject._
import play.api.mvc._

@Singleton
class StrategoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val gb = GameEngine.engine.gb
  def stratego = Action {
    Ok(gb.toString)
  }
}