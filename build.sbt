name := """play25WebApp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
//  jdbc,
//  evolutions,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "com.h2database" % "h2" % "1.4.192",
  "org.webjars.bower" % "compass-mixins" % "0.12.7",
  "org.webjars.bower" % "bootstrap-sass" % "3.3.6",
  "com.adrianhurt" %% "play-bootstrap" % "1.1-P25-B3"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator