package org.browsk.obdii.elm3xx

import jssc.SerialPort
import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent._
import akka.pattern.ask
import akka.event.Logging
import grizzled.slf4j.Logging
import org.browsk.obdii.elm3xx.command._
import scala.language.postfixOps
import scala.util.matching.Regex
import org.browsk.obdii.ParameterId


class ELM3xx(val port : SerialPort) extends Logging {
  implicit val system = ActorSystem("ELM3xx")
  val serialActor = system.actorOf(Props(new SerialHandler(port)), name = "serial_handler")
  val log = Logging(system, this.getClass)
  implicit val timeout = Timeout(2 seconds)
  implicit val ec = ExecutionContext.Implicits.global

  var chipVersion : Option[String]  = Option.empty
  var protocolVersion : Option[String] = Option.empty

	def connect = {
    send(Reset, handleInit _)
	}

  def send(pid: ParameterId, handler: Traversable[String] => Unit) : Future[Any] = {
    send("%02x %02x".format(pid.mode.id, pid.pid), handler)
  }

  def send(command:Command, handler: Traversable[String] => Unit) : Future[Any] = {
    send(command.command, handler)
  }

  def send(message: String, handler: Traversable[String] => Unit) : Future[Any] = {
    val future = (serialActor ask (message)).mapTo[Traversable[String]]
    future map { result => handler(result) }
  }
       //41 00 BE 3E B8 11
  def handleInit(response : Traversable[String]) {
    val regex = new Regex("(ELM\\d*) *(v\\d\\.\\d)")
    val version = response.map(s => regex.findFirstMatchIn(s)).find(m => m.nonEmpty && m.get.matched != None).flatten

    if (version.nonEmpty) {
      chipVersion = Option(version.get.group(1))
      protocolVersion = Option(version.get.group(2))
    }

    log.info("Chip : {}", chipVersion.getOrElse("unknown"))
    log.info("Protocol : {}", protocolVersion.getOrElse("unknown"))

    serialActor.ask(Echo(false))
  }
}