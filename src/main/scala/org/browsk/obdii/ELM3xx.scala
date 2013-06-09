package org.browsk.obdii

import jssc.SerialPort
import akka.actor.{Props, ActorSystem}
import org.browsk.obdii.elm3xx.SerialHandler
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent._
import akka.pattern.ask
import akka.event.Logging
import grizzled.slf4j.Logging
import org.browsk.obdii.elm3xx.command._
import scala.language.postfixOps
import akka.actor.ActorDSL._
import scala.collection.mutable
import scala.util.matching.Regex

class ELM3xx(val port : SerialPort) extends Logging {
  implicit val system = ActorSystem("ELM3xx")
  val serialActor = system.actorOf(Props(new SerialHandler(port)), name = "serial_handler")
  val log = Logging(system, this.getClass)
  implicit val timeout = Timeout(2 seconds)
  implicit val ec = ExecutionContext.Implicits.global

  var chipVersion : Option[String]  = Option.empty
  var protocolVersion : Option[String] = Option.empty

	def connect = {

	  val future = serialActor ? Reset

    val result = Await.result(future, timeout.duration).asInstanceOf[Traversable[String]]

    val regex = new Regex("(ELM\\d*) *(v\\d\\.\\d)")
    val version = result.map(s => regex.findFirstMatchIn(s)).find(m => m.nonEmpty && m.get.matched != None).flatten

    if (version.nonEmpty) {
      chipVersion = Option(version.get.group(1))
      protocolVersion = Option(version.get.group(2))
    }

    log.info("Chip : {}", chipVersion.getOrElse("unknown"))
    log.info("Protocol : {}", protocolVersion.getOrElse("unknown"))

    serialActor.ask(Echo(false)).mapTo[Array[String]]


	}
}