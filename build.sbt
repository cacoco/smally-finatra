import com.typesafe.sbt.SbtNativePackager._

enablePlugins(JavaAppPackaging)

name := "smally-finatra"
organization := "io.angstrom.smally"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.1"

mainClass in Compile := Some("io.angstrom.smally.SmallyServerMain")
fork in run := true

lazy val versions = new {
  val finatra = "17.10.0"
  val guice = "4.0"
  val logback = "1.1.7"
  val mockito = "1.9.5"
  val redis = "2.7.2"
  val scalacheck = "1.13.4"
  val scalatest = "3.0.0"
  val slf4j = "1.7.21"
  val specs2 = "2.4.17"
}

/* Necessary "test-jar" dependencies of Finatra libraries 
   see: https://twitter.github.io/finatra/index.html#test-dependencies */
val libraryTestDependencies = Seq(
  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests")

libraryDependencies ++= libraryTestDependencies ++ Seq(
  "ch.qos.logback" % "logback-classic" % versions.logback,
  
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",
  "com.google.inject" % "guice" % versions.guice % "test",
  
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-jackson" % versions.finatra,
  
  "org.mockito" % "mockito-core" % versions.mockito % "test",
  
  "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test",
  
  "org.scalatest" %% "scalatest" %  versions.scalatest % "test",
    
  "org.specs2" %% "specs2-mock" % versions.specs2 % "test",
  
  "redis.clients" % "jedis" % versions.redis)
