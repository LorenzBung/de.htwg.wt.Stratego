// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")
//addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")

// Vue support
resolvers += Resolver.bintrayRepo("givers", "maven")
addSbtPlugin("givers.vuefy" % "sbt-vuefy" % "4.0.0")