val dottyVersion = "0.1.2-RC1"
val lwjglVersion = "2.9.3"

lazy val root = (project in file("."))
    .settings(
      name := "InnerClassIAE",
      version := "0.1",

      scalaVersion := dottyVersion,
      //scalaVersion := "2.11.11",
      //LWJGLPlugin.lwjgl.version := lwjglVersion,
    
      fullRunTask(TaskKey[Unit]("run-hello-input"), Test, "HelloInput"),


      libraryDependencies ++= Seq(
        "com.novocode" % "junit-interface" % "0.11" % "test"
        ,"org.jmonkeyengine" % "jme3-core" % "3.1.0-stable" exclude("org.jmonkeyengine", "jme3-testdata")
        ,"org.jmonkeyengine" % "jme3-desktop" % "3.1.0-stable" exclude("org.jmonkeyengine", "jme3-testdata")
        ,"org.jmonkeyengine" % "jme3-lwjgl" % "3.1.0-stable" exclude("org.jmonkeyengine", "jme3-testdata")
      )
  )
