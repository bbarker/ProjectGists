val scala211 = "2.11.8"
val scala212 = "2.12.6"
val scalaVersionSelect = scala212

val akkaHttpDep = "com.typesafe.akka" %% "akka-http" % "10.0.9"
val ammoniteDep = "com.lihaoyi" %% "ammonite-ops" % "1.0.1"

val scalatest = Def.setting(
  "org.scalatest" %%% "scalatest" % "3.2.0-SNAP7" % "test")

val cats = Def.setting(
  "org.typelevel" %%% "cats" % "0.9.0"
)

val boopickleVersion = "1.3.0"
val boopickleDeps = Def.setting(Seq(
  "io.suzaku" %%% "boopickle" % boopickleVersion
  ,"io.suzaku" %%% "boopickle-shapeless" % boopickleVersion
))

val autowireDeps = Def.setting(
  // Seq("com.lihaoyi" %%% "autowire" % "0.2.6")
  Seq("de.daxten" %%% "autowire" % "0.3.1")
    ++ boopickleDeps.value
)

val mhtmlDeps = Def.setting(Seq(
  "in.nvilla" %%% "monadic-html" % "0.4.0-RC1",
  "in.nvilla" %%% "monadic-rx-cats" % "0.4.0-RC1",
  "org.scala-js" %%% "scalajs-dom" % "0.9.2"
))

val circeVersion = "0.10.0-M1"
val circeDeps = Def.setting(Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic"
//  "io.circe" %% "circe-parser" // Don't need so far
).map(_ % circeVersion))

val upickleVersion = "0.6.5"

val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  scalaVersion := scalaVersionSelect,
  scalacOptions ++= Seq(
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
    // "-Xplugin-require:macroparadise", // For scalameta version only?
    "-Xfuture"),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("public")
    ,Resolver.sonatypeRepo("snapshots")
    ,Resolver.bintrayRepo("daxten", "maven")
    ,"amateras-repo" at "http://amateras.sourceforge.jp/mvn/" // For ace editor facade
    //,"sonatype-staging" at "https://oss.sonatype.org/content/repositories/staging/"
  ),
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
  // Shared /config between all projects
  unmanagedClasspath in Compile <+= (baseDirectory) map { bd => Attributed.blank(bd / ".." / "config") },
  unmanagedClasspath in Runtime <++= (unmanagedClasspath in Compile),
  unmanagedClasspath in Test <++= (unmanagedClasspath in Compile),
  libraryDependencies ++= Seq(
    "org.typelevel"  %%% "squants"  % "1.3.0"
    ,"com.lihaoyi" %%% "upickle" % upickleVersion
  ) ++ circeDeps.value
  ,wartremoverErrors ++= Warts.allBut(Wart.ImplicitParameter, Wart.NoNeedForMonad, Wart.Overloading)
) // ++ warnUnusedImport // Disabling during normal dev - too annoying!

autoCompilerPlugins := true

addCompilerPlugin( // For circe generic:
  "org.scalamacros" % s"paradise_$scalaVersionSelect" % "2.1.1" /*cross CrossVersion.full*/
)
val settingsJVM = commonSettings

val settingsJS = Seq(

  emitSourceMaps := true,   
  //persistLauncher in Compile := true,   
  //persistLauncher in Test := false,   
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

lazy val `web-client` = project
  .enablePlugins(ScalaJSPlugin)
  .settings(settingsJS: _*)
  .settings(scalaJSUseMainModuleInitializer := true)
  .settings(libraryDependencies ++=
    Seq(scalatest.value)
    ++ autowireDeps.value ++ mhtmlDeps.value
    ++ Seq(
      // alternative at https://github.com/DefinitelyScala/scala-js-ace
      "com.scalawarrior" %%% "scalajs-ace" % "0.0.4"
      ,"com.github.japgolly.scalacss" %%% "core" % "0.5.5"
    )
  )
  //.settings(jsDependencies ++= reactDeps)
  .dependsOn(modelJS)

lazy val `web-server` = project
  .settings(settingsJVM: _*)
  // Include web-static in resources for static serving
  .settings(unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "web-static")
  // Remove scala.js target from file watch to prevent compilation loop
  .settings(watchSources := watchSources.value.filterNot(_.getPath.contains("target")))
  // Add web-client sources to file watch
  .settings(watchSources <++= (watchSources in `web-client`))
  // Make compile depend on Scala.js fast compilation
  .settings(compile <<= (compile in Compile) dependsOn (fastOptJS in Compile in `web-client`))
  // Make re-start depend on Scala.js fast compilation
  .settings(reStart <<= reStart dependsOn (fastOptJS in Compile in `web-client`))
  // Make assembly depend on Scala.js full optimization
  .settings(assembly <<= assembly dependsOn (fullOptJS in Compile in `web-client`))
  .settings(libraryDependencies ++=
    Seq(
      akkaHttpDep, ammoniteDep, scalatest.value
      ,"com.lihaoyi" %% "ujson-circe" % upickleVersion
      ,"org.scalaz" %% "scalaz-zio" % "0.1-SNAPSHOT"
    ) ++ autowireDeps.value
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
