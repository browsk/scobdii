package org.browsk.obdii.elm3xx.command

class Echo(val on: Boolean) extends Command {
  def command: String = "ATE" + (if (on) "1" else "0")
}
