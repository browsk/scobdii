import jssc.SerialPortList
import jssc.SerialPort
import org.browsk.obdii.ELM3xx

object Scobdii {
  def main(args: Array[String]) {
    println("All good...")
        
    SerialPortList.getPortNames().foreach(println)
    
    val port = new SerialPort("/dev/ttyUSB0")
    port.openPort();
    port.setParams(115200, 8, 1, 0)
    
    val device = new ELM3xx(port)
    
    device.connect
  }
}
