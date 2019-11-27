package controllers

import de.htwg.se.stratego.controller.{CellChanged, GameEngine}
import javax.inject._
import play.api.mvc._
import akka.actor.ActorSystem
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.actor._

import scala.swing.Reactor

@Singleton
class StrategoController @Inject()(cc: ControllerComponents) (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  val eng = GameEngine.engine
  def stratego = Action {
    Ok(views.html.stratego(eng))
  }
  def newGame = Action {
    GameEngine.engine.newGame()
    Ok(de.htwg.se.stratego.controller.GameEngine.engine.toJson())
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


  def websocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      println("Connect received")
      StrategoWebSocketActorFactory.create(out)
    }
  }

  object StrategoWebSocketActorFactory {
    def create(out: ActorRef) = {
      Props(new StrategoWebSocketActor(out))
    }
  }

  class StrategoWebSocketActor(out: ActorRef) extends Actor with Reactor {
    listenTo(eng)

    def receive = {
      case msg: String =>
        out ! (eng.toJson.toString)
        println("Sent Json to Client"+ msg)
    }

    reactions += {
      case event: CellChanged     => sendJsonToClient
    }

    def sendJsonToClient = {
      out ! (eng.toJson.toString)
    }
  }

}