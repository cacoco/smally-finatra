import com.typesafe.sbt.SbtNativePackager._

packageArchetype.java_application

name := "smally-finatra"
organization := "io.angstrom.smally"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

fork in run := true

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com")

lazy val versions = new {
  val finatra = "2.0.1"
  val logback = "1.0.13"
  val mockito = "1.9.5"
  val redis = "2.7.2"
  val scalatest = "2.2.3"
  val specs2 = "2.3.12"
}

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % versions.logback,
  "ch.qos.logback" % "logback-classic" % versions.logback % "test",

  "com.twitter.finatra" %% "finatra-http" % versions.finatra,
  "com.twitter.finatra" %% "finatra-slf4j" % versions.finatra,

  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test",
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test",
  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2" % versions.specs2 % "test",

  "redis.clients" % "jedis" % versions.redis)
