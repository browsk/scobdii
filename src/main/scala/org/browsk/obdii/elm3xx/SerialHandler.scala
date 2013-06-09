package org.browsk.obdii.elm3xx

import akka.actor.{ActorRef, ActorLogging, Actor}
import jssc.SerialPort
import org.browsk.obdii.elm3xx.command.Command
import scala.collection.mutable

case class ReceivedMessage(val message:String)

class SerialHandler(val port:SerialPort) extends Actor with ActorLogging {

  port.addEventListener(new SerialPortEventReader(port, self), SerialPort.MASK_RXCHAR)

  var q: ActorRef = null

  def receive = {
    case r : ReceivedMessage => {
      log.debug("RX : {}", r.message)
      q ! r.message
        .split(Array('\n', '\r'))
        .filter(l => l.nonEmpty)
        .toSeq
   //     .asInstanceOf[mutable.WrappedArray[String]]
    }
    case c : Command => {
      log.debug("TX : {}", c.command)
      port.writeString(c.command)
      port.writeByte(13)
      q = sender
    }
  }
}
