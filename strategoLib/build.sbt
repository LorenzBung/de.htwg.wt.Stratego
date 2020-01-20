name          := "htwg.se.stratego"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.12.4"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

connectInput in run := true

resolvers += Resolver.jcenterRepo

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3"
libraryDependencies += "junit" % "junit" % "4.8" % "test"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.10"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.1"