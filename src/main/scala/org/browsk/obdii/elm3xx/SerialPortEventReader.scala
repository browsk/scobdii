package org.browsk.obdii.elm3xx

import jssc.{SerialPortEvent, SerialPortEventListener, SerialPort}
import akka.actor.ActorRef
import grizzled.slf4j.Logging

class SerialPortEventReader(val port : SerialPort, val receiver: ActorRef) extends SerialPortEventListener with Logging {
  var buffer = new StringBuilder
  def serialEvent(event: SerialPortEvent) {
    if (event.isRXCHAR) {

      val count = event.getEventValue

      val data = port.readBytes(count)

      logger.debug("Serial RX event with " + count + " chars")

      data.foreach(c =>
        {
          if (c == '>') {
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
