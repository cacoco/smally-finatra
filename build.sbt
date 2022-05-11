import com.typesafe.sbt.SbtNativePackager._

enablePlugins(JavaAppPackaging)

name := "smally-finatra"
organization := "io.angstrom.smally"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.15"

Compile / mainClass := Some("io.angstrom.smally.SmallyServerMain")
run / fork := true

lazy val versions = new {
  val twitter = "22.4.0"
  val guice = "4.2.3"
  val logback = "1.2.8"
  val redis = "4.2.3"
  val scalacheck = "1.15.4"
  val scalatest = "3.1.2"
  val slf4j = "1.7.30"
}

/* Necessary "test-jar" dependencies of Finatra libraries 
   see: https://twitter.github.io/finatra/index.html#test-dependencies */
val libraryTestDependencies = Seq(
  "com.twitter" %% "finatra-http-server" % versions.twitter % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.twitter % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.twitter % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.twitter % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.twitter % "test" classifier "tests")

libraryDependencies ++= libraryTestDependencies ++ Seq(
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",
  "com.google.inject" % "guice" % versions.guice % "test",
  "com.twitter" %% "util-mock" % versions.twitter % "test",
  "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test",
  "org.scalatest" %% "scalatest" %  versions.scalatest % "test",
  
  "ch.qos.logback" % "logback-classic" % versions.logback,
  
  "com.twitter" %% "finagle-http" % versions.twitter,
  "com.twitter" %% "finatra-http-annotations" % versions.twitter,
  "com.twitter" %% "finatra-http-server" % versions.twitter,
  "com.twitter" %% "finatra-jackson" % versions.twitter,
  "com.twitter" %% "inject-app" % versions.twitter,
  "com.twitter" %% "twitter-server-logback-classic" % versions.twitter,
  "com.twitter" %% "util-slf4j-api" % versions.twitter,
  "com.twitter" %% "util-stats" % versions.twitter,
  
  "redis.clients" % "jedis" % versions.redis)
