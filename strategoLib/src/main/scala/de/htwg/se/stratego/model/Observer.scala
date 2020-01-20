package de.htwg.se.stratego.model

trait Observer[S] {
  def receiveUpdate(subject: S)
}
