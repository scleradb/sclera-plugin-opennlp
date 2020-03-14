name := "sclera-plugin-opennlp"

description := "Add-on that enables Sclera to perform text-processing using the OpenNLP library from within SQL"

version := "4.0-SNAPSHOT"

homepage := Some(url("https://github.com/scleradb/sclera-plugin-opennlp"))

organization := "com.scleradb"

organizationName := "Sclera, Inc."

organizationHomepage := Some(url("https://www.scleradb.com"))

startYear := Some(2012)

scalaVersion := "2.13.1"

licenses := Seq("Apache License version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

libraryDependencies ++= Seq(
    "org.apache.opennlp" % "opennlp-tools" % "1.9.1",
    "com.scleradb" %% "sclera-config" % "4.0-SNAPSHOT" % "provided",
    "com.scleradb" %% "sclera-core" % "4.0-SNAPSHOT" % "provided",
    "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)

scalacOptions ++= Seq(
    "-Werror", "-feature", "-deprecation", "-unchecked"
)

exportJars := true

fork in Test := true
