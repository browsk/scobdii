package org.browsk.obdii

import jssc.{SerialPortEvent, SerialPort, SerialPortEventListener}
import scala.collection.mutable
import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import org.browsk.obdii.elm3xx.{SerialHandler, SerialPortEventReader, ReceiveActor}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent._
import akka.pattern.ask
import grizzled.slf4j.Logging
import akka.event.Logging
import grizzled.slf4j.Logging

class ELM3xx(val port : SerialPort) extends Logging {
  val system = ActorSystem("ELM3xx")
  val serialActor = system.actorOf(Props(new SerialHandler(port)), name = "serial_handler")
  val log = Logging(system, this.getClass)
  implicit val timeout = Timeout(2 seconds)
  implicit val ec = ExecutionContext.Implicits.global

	def connect = {

	  val future = serialActor ? "ATZ\r\n"

    val result = Await.result(future, timeout.duration).asInstanceOf[String]

    log.info("Connect got this : {}", result)
	  
	  Thread.sleep(2000)

	 // val p = port.readString()
//	  println(p)
	}
}