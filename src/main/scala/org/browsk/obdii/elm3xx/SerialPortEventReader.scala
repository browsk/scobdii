package org.browsk.obdii.elm3xx

import jssc.{SerialPortEvent, SerialPortEventListener, SerialPort}
import akka.actor.ActorRef

class SerialPortEventReader(val port : SerialPort, val receiver: ActorRef) extends SerialPortEventListener {
  var buffer = new StringBuilder
  def serialEvent(event: SerialPortEvent) {
    if (event.isRXCHAR) {
      val count = event.getEventValue

      val data = port.readBytes(count)

      data.foreach(c =>
        {
          if (c == '\r') {
            if (buffer.nonEmpty) {
              receiver ! new ReceivedMessage(buffer.toString)
              buffer.clear
            }
          }
          else {
            buffer += c.toChar
          }
        }
      )
    }
  }
}
