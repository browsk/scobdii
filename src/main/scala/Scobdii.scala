import akka.actor.{Props, ActorSystem}
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import grizzled.slf4j.Logging
import jssc.SerialPortList
import jssc.SerialPort
import org.browsk.obdii.elm3xx.{ELM3xx, SerialHandler}
import org.browsk.obdii.{SupportedPIDs, ParameterId}
import org.browsk.obdii.ui.{SerialMonitor, RawPane}
import org.slf4j
import org.slf4j._
import org.slf4j.LoggerFactory
import scala.swing._
import scala.swing.TabbedPane.Page

object Scobdii extends SimpleSwingApplication with Logging {
  val port = new SerialPort("/dev/ttyUSB0")

  try {
    port.openPort();
    port.setParams(115200, 8, 1, 0)

  }
  catch {
    case e: Exception => error("Failed to open serial port", e)
  }
  val device = new ELM3xx(port)

  implicit val system = ActorSystem()
  val serialMonitor = system.actorOf(Props[SerialMonitor], name = "serial_monitor")

  def top = new MainFrame {

    title = "OBD-II Interface"

    menuBar = new MenuBar {
      contents += new Menu("ELM3xx") {
        contents += new MenuItem(Action("Connect") {
          device.connect
        })
        contents += new MenuItem(Action("Query PIDs") {
          device.send(new SupportedPIDs(), {
            result: Traversable[String] => logger.debug(result)
          })
        })
      }
    }


    contents = new BorderPanel {
      import BorderPanel.Position._

      val tabs = new TabbedPane {
        import TabbedPane._

        val rawPane = new RawPane
        serialMonitor ! rawPane.textArea
        pages += new Page("Raw", rawPane)
      }

      serialMonitor ! "hello"

      val list = new ListView(tabs.pages) {
        selectIndices(0)
        selection.intervalMode = ListView.IntervalMode.Single
        renderer = ListView.Renderer(_.title)
      }
      val center = new SplitPane(Orientation.Vertical, new ScrollPane(list), tabs) {
        oneTouchExpandable = true
        continuousLayout = true
      }
      layout(center) = Center
    }
    // assume SLF4J is bound to logback in the current environment
   // val lc : LoggerContext = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
    // print logback's internal status
  //  StatusPrinter.print(lc)

    println("All good...")
    size = new Dimension(320,400)

  }
}
