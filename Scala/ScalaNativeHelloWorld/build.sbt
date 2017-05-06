

name := "ScalaNativeHelloWorld"

version := "1.0"

scalaVersion := "2.11.11"


lazy val hello = (project in file("."))
.enablePlugins(ScalaNativePlugin)


        