
val scalacMajor  = "2.11"
val dottyVersion = "0.2.0-bin-20170624-281416b-NIGHTLY"

lazy val root = (project in file("."))
  .settings(
    name := "FinalClassError",
    version := "0.1",
    scalaVersion := dottyVersion,
    // scalaVersion := "2.11.11",
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at
	"https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at
	"https://oss.sonatype.org/content/repositories/releases"
    )
    ,libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
)



