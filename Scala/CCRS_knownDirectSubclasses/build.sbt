val scala211 = "2.11.8"
val scala212 = "2.12.4"
val scalaVersionSelect = scala212

val akkaHttpDep = "com.typesafe.akka" %% "akka-http" % "10.0.9"
val ammoniteDep = "com.lihaoyi" %% "ammonite-ops" % "1.0.1"

val scalatest = Def.setting(
  "org.scalatest" %%% "scalatest" % "3.2.0-SNAP7" % "test")

val cats = Def.setting(
  "org.typelevel" %%% "cats" % "0.9.0"
)

val autowireDeps = Def.setting(Seq(   
  "com.lihaoyi" %%% "autowire" % "0.2.6",
  "io.suzaku" %%% "boopickle" % "1.2.6"))

val mhtmlDeps = Def.setting(Seq(
  "in.nvilla" %%% "monadic-html" % "0.4.0-RC1",
  "in.nvilla" %%% "monadic-rx-cats" % "0.4.0-RC1",
  "org.scala-js" %%% "scalajs-dom" % "0.9.2"
))

val circeVersion = "0.8.0"
val circeDeps = Def.setting(Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic"
//  "io.circe" %% "circe-parser" // Don't need so far
).map(_ % circeVersion))

val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  scalaVersion := scalaVersionSelect,
  scalacOptions := Seq(
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation:false",
    "-Xfatal-warnings",
    //"-Xlint",
    "-Xlint:-unused,_",
    // "-Ywarn-unused:imports", // Disabling during normal dev - too annoying!
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("public")
    ,"amateras-repo" at "http://amateras.sourceforge.jp/mvn/" // For ace editor facade
  ),
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
  // Shared /config between all projects
  unmanagedClasspath in Compile <+= (baseDirectory) map { bd => Attributed.blank(bd / ".." / "config") },
  unmanagedClasspath in Runtime <++= (unmanagedClasspath in Compile),
  unmanagedClasspath in Test <++= (unmanagedClasspath in Compile),
  libraryDependencies ++= Seq(
    "org.typelevel"  %%% "squants"  % "1.3.0"
  )
) // ++ warnUnusedImport // Disabling during normal dev - too annoying!

val settingsJVM = commonSettings

val settingsJS = Seq(

  emitSourceMaps := true,   
  persistLauncher in Compile := true,   
  persistLauncher in Test := false,   
  skip in packageJSDependencies := false,   
  // Uniformises fastOptJS/fullOptJS output file name   
  artifactPath in (Compile, fastOptJS) :=   
    ((crossTarget in (Compile, fastOptJS)).value / ((moduleName in fastOptJS).value + "-opt.js"))   
) ++ commonSettings ++
  // Specifies where to store the outputs of Scala.js compilation.
  Seq(fullOptJS, fastOptJS, packageJSDependencies, packageScalaJSLauncher, packageMinifiedJSDependencies)   
    .map(task => crossTarget in (Compile, task) := file("web-static/static/target"))

lazy val model = crossProject   
  .crossType(CrossType.Pure)   
  .settings(commonSettings: _*)
  .settings(libraryDependencies += scalatest.value)   
lazy val modelJVM = model.jvm
lazy val modelJS = model.js

lazy val `web-server` = project
  .settings(settingsJVM: _*)
  // Include web-static in resources for static serving
  .settings(unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "web-static")
  // Remove scala.js target from file watch to prevent compilation loop
  .settings(watchSources := watchSources.value.filterNot(_.getPath.contains("target")))
  // Add web-client sources to file watch
  .settings(libraryDependencies ++=
    Seq(akkaHttpDep, ammoniteDep, scalatest.value) ++ autowireDeps.value ++ circeDeps.value
  )
  .dependsOn(modelJVM)

lazy val warnUnusedImport = Seq(
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 10)) => Seq()
      case _ => Seq("-Ywarn-unused-import")
    }
  },
  scalacOptions in (Compile, console) ~= {_.filterNot("-Ywarn-unused-import" == _)},
  scalacOptions in (Test, console) <<= (scalacOptions in (Compile, console)))
