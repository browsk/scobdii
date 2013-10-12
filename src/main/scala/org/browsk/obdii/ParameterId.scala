package org.browsk.obdii

trait ParameterId {
  def mode : Mode
  def pid: Int
  def description: String
}

class SupportedPIDs extends ParameterId {
  val mode = ShowCurrent
  val pid = 0
  val description = "PIDs supported"
}