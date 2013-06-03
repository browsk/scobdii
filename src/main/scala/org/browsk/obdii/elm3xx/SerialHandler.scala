package org.browsk.obdii.elm3xx

import akka.actor.{ActorRef, ActorLogging, Actor}
import jssc.SerialPort

case class ReceivedMessage(val message:String)

class SerialHandler(val port:SerialPort) extends Actor with ActorLogging {

  port.addEventListener(new SerialPortEventReader(port, self), SerialPort.MASK_RXCHAR)

  var q: ActorRef = null

  def receive = {
    case r : ReceivedMessage => {
      log.debug("RX : {}", r.message)
      q ! r.message
    }
    case s : String => {
      log.debug("TX : {}", s)
      port.writeString(s)
      q = sender
    }
  }
}
