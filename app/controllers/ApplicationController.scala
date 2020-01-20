package controllers

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.{ HandlerResult, LogoutEvent }
import com.mohiva.play.silhouette.api.actions._
import com.mohiva.play.silhouette.impl.providers.GoogleTotpInfo
import javax.inject.Inject
import play.api.mvc._
import utils.route.Calls
import de.htwg.se.stratego.controller.{ CellChanged, GameEngine }
import de.htwg.se.stratego.model.boardComponent.Coordinates
import models.User
import play.api.libs.streams.ActorFlow

import scala.concurrent.{ ExecutionContext, Future }
import scala.swing.Reactor

/**
 * The basic application controller.
 */
class ApplicationController @Inject() (
  scc: SilhouetteControllerComponents,
  home: views.html.home,
  aboutView: views.html.about,
  strategoView: views.html.stratego
)(
  implicit
  ex: ExecutionContext,
  mat: Materializer,
  system: ActorSystem
) extends SilhouetteController(scc) {

  val eng = GameEngine.engine

  /**
   * Handles the index action.
   *
   * @return The result to display.
   */
  def index = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      Ok(home(request.identity, totpInfoOpt))
    }
  }

  def about = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      Ok(aboutView(request.identity, totpInfoOpt))
    }
  }

  def stratego = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      Ok(strategoView(request.identity, totpInfoOpt))
    }
  }

  def newGame = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      eng.newGame()
      Ok(eng.toJson())
    }
  }

  def set(x: Int, y: Int) = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      val coords = Coordinates(x, y)
      eng.set(coords)
      Ok(eng.get(coords).toString())
    }
  }

  def unset(x: Int, y: Int) = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      val coords = Coordinates(x, y)
      eng.unset(coords)
      Ok(eng.get(coords).toString())
    }
  }

  def select(strength: Int) = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      eng.selectFigure(strength)
      Ok(strategoView(request.identity, totpInfoOpt))
    }
  }

  def move(x1: Int, y1: Int, x2: Int, y2: Int) = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      val from = de.htwg.se.stratego.model.boardComponent.Coordinates(x1, y1)
      val to = de.htwg.se.stratego.model.boardComponent.Coordinates(x2, y2)
      eng.move(from, to)
      Ok(strategoView(request.identity, totpInfoOpt))
    }
  }

  def json = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authInfoRepository.find[GoogleTotpInfo](request.identity.loginInfo).map { totpInfoOpt =>
      Ok(eng.toJson())
    }
  }
  def websocket = WebSocket.acceptOrResult[String, String] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    silhouette.SecuredRequestHandler { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Right(ActorFlow.actorRef(StrategoWebSocketActorFactory.create(user)))
      case HandlerResult(r, None) => Left(r)
    }
  }

  object StrategoWebSocketActorFactory {
    def create(user: User)(out: ActorRef) = {
      Props(new StrategoWebSocketActor(user, out))
    }
  }

  class StrategoWebSocketActor(user: User, out: ActorRef) extends Actor with Reactor {
    listenTo(eng)

    def receive = {
      case msg: String =>
        out ! (eng.toJson.toString)
        println("Sent Json to Client" + msg)
    }

    reactions += {
      case event: CellChanged => sendJsonToClient()
    }

    def sendJsonToClient() = {
      out ! (eng.toJson.toString)
    }
  }

  /**
   * Handles the Sign Out action.
   *
   * @return The result to display.
   */
  def signOut = SecuredAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    val result = Redirect(Calls.home)
    eventBus.publish(LogoutEvent(request.identity, request))
    authenticatorService.discard(request.authenticator, result)
  }
}
