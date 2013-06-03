package org.browsk.obdii.elm3xx

import akka.actor.{ActorLogging, Actor}

class ReceiveActor extends Actor with ActorLogging {
  def receive = {
    case message: String => {
      log.info("Received message : {}", message)
    }
  }
}
