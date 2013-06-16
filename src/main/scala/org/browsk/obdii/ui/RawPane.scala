package org.browsk.obdii.ui

import scala.swing._


class RawPane extends FlowPanel {
  val textArea = new TextArea
  contents += new Label("Raw Comms")
  contents += textArea

  border = Swing.EmptyBorder(5, 5, 5, 5)
}
