package org.browsk.obdii.elm3xx.command

/**
 * Created with IntelliJ IDEA.
 * User: brett
 * Date: 7/06/13
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */

trait Command {
  def command : String
}

object Reset extends Command {
  def command: String = "ATZ"
}
