package org.browsk.obdii

sealed trait Mode { def id: Int }

case object ShowCurrent extends Mode{val id = 1}

case object ShowFreezeFrame extends Mode{val id = 2}
