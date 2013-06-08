name := "scobdii"

version := "0.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.12"

scalaVersion := "2.10.1"

libraryDependencies += "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.4"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.1.4"

scalacOptions ++= Seq("-feature")