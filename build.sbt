import com.typesafe.sbt.SbtNativePackager._
import NativePackagerKeys._

packageArchetype.java_application

name := "smally-finatra"
organization := "io.angstrom.smally"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "http://maven.twttr.com",
  "Finatra Repo" at "http://twitter.github.com/finatra"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-redis" % "6.25.0",

  "com.twitter.finatra" %% "finatra-http" % "2.0.0.M2",
  "com.twitter.finatra" %% "finatra-logback" % "2.0.0.M2",

  "com.twitter.finatra" %% "finatra-http" % "2.0.0.M2" % "test",
  "com.twitter.finatra" %% "finatra-jackson" % "2.0.0.M2" % "test",
  "com.twitter.inject" %% "inject-server" % "2.0.0.M2" % "test",
  "com.twitter.inject" %% "inject-app" % "2.0.0.M2" % "test",
  "com.twitter.inject" %% "inject-core" % "2.0.0.M2" % "test",
  "com.twitter.inject" %% "inject-modules" % "2.0.0.M2" % "test",
  "com.twitter.finatra" %% "finatra-http" % "2.0.0.M2" % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-jackson" % "2.0.0.M2" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % "2.0.0.M2" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-app" % "2.0.0.M2" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % "2.0.0.M2" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % "2.0.0.M2" % "test" classifier "tests",

  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.3" % "test",
  "org.specs2" %% "specs2" % "2.3.12" % "test")
