name := """todolist"""
organization := "com.scala_todolist"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.17"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += evolutions

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
//libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.1"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
