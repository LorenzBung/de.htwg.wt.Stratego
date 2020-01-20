package de.htwg.se.stratego

import com.google.inject.AbstractModule
import de.htwg.se.stratego.model.boardComponent.{ GameBoard, GameBoardInterface }
import de.htwg.se.stratego.model.fileIoComponent._
import net.codingwell.scalaguice.ScalaModule

class StrategoModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[GameBoardInterface].to[GameBoard]
    bind[FileIOInterface].to[fileIoJsonImpl.FileIO]
  }
}
