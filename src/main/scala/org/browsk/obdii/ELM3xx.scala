package org.browsk.obdii

import jssc.{SerialPortEvent, SerialPort, SerialPortEventListener}
import scala.collection.mutable


class SerialPortEventReader(val port : SerialPort) extends SerialPortEventListener {
  var buffer = new StringBuilder
  def serialEvent(event: SerialPortEvent) {
    if (event.isRXCHAR) {
      val count = event.getEventValue

      val data = port.readBytes(count)

      data.foreach(c =>

        {
          if (c == '\r' && buffer.nonEmpty) {
            println("Got " + buffer)
            buffer.clear
          }
          else {
            buffer += c.toChar
          }
        }
      )
    }
  }
}



class ELM3xx(val port : SerialPort) {
	def connect = {
	
	  val mask = SerialPort.MASK_RXCHAR;
	  
	  port.setEventsMask(mask)

    port.addEventListener(new SerialPortEventReader(port))
	  
	  port.writeString("ATZ\r\n")
	  
	  Thread.sleep(2000)
	  
	 // val p = port.readString()
//	  println(p)
	}
}