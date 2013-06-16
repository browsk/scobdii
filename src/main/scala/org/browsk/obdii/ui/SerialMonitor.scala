package org.browsk.obdii.ui

import akka.actor.Actor
import scala.swing.{Swing, TextComponent, Component}
import scala.collection.mutable.ListBuffer

class SerialMonitor extends Actor {
  var listeners = ListBuffer[TextComponent]()

  def receive = {
    case component : TextComponent => listeners += component
    case s : String => listeners foreach(l => Swing.onEDT(l.text += s))
  }
}
