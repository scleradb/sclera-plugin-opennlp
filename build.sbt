name := "sclera-plugin-opennlp"

description := "Add-on that enables Sclera to perform text-processing using the OpenNLP library from within SQL"

homepage := Some(url(s"https://github.com/scleradb/${name.value}"))

scmInfo := Some(
    ScmInfo(
        url(s"https://github.com/scleradb/${name.value}"),
        s"scm:git@github.com:scleradb/${name.value}.git"
    )
)

version := "4.0-SNAPSHOT"

startYear := Some(2012)

scalaVersion := "2.13.1"

licenses := Seq("Apache License version 2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
    "org.apache.opennlp" % "opennlp-tools" % "1.9.1",
    "com.scleradb" %% "sclera-config" % "4.0-SNAPSHOT" % "provided",
    "com.scleradb" %% "sclera-core" % "4.0-SNAPSHOT" % "provided",
    "org.scalatest" %% "scalatest" % "3.1.1" % "test"
)

scalacOptions ++= Seq(
    "-Werror", "-feature", "-deprecation", "-unchecked"
)

exportJars := true

fork in Test := true
