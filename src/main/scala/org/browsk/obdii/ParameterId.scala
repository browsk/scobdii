package org.browsk.obdii

class ParameterId(val mode:Byte, val pid: Short, val bytes:Byte, val description:String) {
  def equals(that: ParameterId) : Boolean =
    mode == that.mode && pid == that.pid
}

case class SupportedPIDs() extends ParameterId(1, 0, 4, "PIDs supported") {
}

//class Parameter