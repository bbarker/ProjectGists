name := "SquantsConversions"

version := "1.0"

scalaVersion := "2.12.2"

resolvers ++= Seq(
  Resolver.sonatypeRepo("public")
)

libraryDependencies += "org.typelevel"  %% "squants"  % "1.3.0-SNAPSHOT"