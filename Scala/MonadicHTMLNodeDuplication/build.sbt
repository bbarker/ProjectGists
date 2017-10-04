enablePlugins(ScalaJSPlugin)

name := "MHTMLNodeDup"

scalaVersion := "2.12.3" // or any other Scala version >= 2.10.2
lazy val mhtmlV = "0.3.2"

//libraryDependencies += "in.nvilla" %%% "monadic-rx-cats" % mhtmlV
libraryDependencies += "in.nvilla" %%% "monadic-html" % mhtmlV
libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.4.4"

requiresDOM in Test := true
scalaJSUseMainModuleInitializer := true

