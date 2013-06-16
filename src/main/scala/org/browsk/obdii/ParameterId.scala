package org.browsk.obdii

trait ParameterId {
  def mode : Mode
  def pid: Int
  def description: String
}

case class SupportedPIDs(val mode: Mode = ShowCurrent, val pid : Int = 0, val description : String = "PIDs supported") {
}
