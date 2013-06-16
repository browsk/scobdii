package org.browsk.obdii

sealed trait Mode { def id: Int }

case object ShowCurrent extends Mode{val id = 0}

case object ShowFreezeFrame extends Mode{val id = 1}
