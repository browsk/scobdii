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

    val result = Await.result(future, timeout.duration).asInstanceOf[Array[String]]

    val version = result.find(s => s.matches("ELM.* v.*"))

    if (version.nonEmpty) {
      chipVersion = new Regex("ELM\\d*") findFirstIn version.get
      protocolVersion = new Regex("v\\d\\.\\d") findFirstIn version.get
    }

    log.info("Chip : {}", chipVersion.getOrElse("unknown"))
    log.info("Protocol : {}", protocolVersion.getOrElse("unknown"))

    serialActor.ask(new Echo(false)).mapTo[Array[String]]


	}
}