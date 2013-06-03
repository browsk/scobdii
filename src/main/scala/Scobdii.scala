import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import grizzled.slf4j.Logging
import jssc.SerialPortList
import jssc.SerialPort
import org.browsk.obdii.ELM3xx
import org.slf4j
import org.slf4j._
import org.slf4j.LoggerFactory

object Scobdii extends Logging {
  def main(args: Array[String]) {

    // assume SLF4J is bound to logback in the current environment
   // val lc : LoggerContext = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
    // print logback's internal status
  //  StatusPrinter.print(lc)

    println("All good...")
        
    val port = new SerialPort("/dev/ttyUSB0")

    try {
      port.openPort();
      port.setParams(115200, 8, 1, 0)

    }
    catch {
      case e: Exception => error("Failed to open serial port", e)
    }
    val device = new ELM3xx(port)
    
    device.connect
  }
}
