
val scalacMajor  = "2.11"
val dottyVersion = "0.2.0-bin-20170620-1d05796-NIGHTLY"

lazy val root = (project in file("."))
  .settings(
    name := "DottyCatsHelloWorld",
    version := "0.1",
    scalaVersion := dottyVersion,
    // scalaVersion := "2.11.11",
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at
	"https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at
	"https://oss.sonatype.org/content/repositories/releases"
    )
    ,libraryDependencies += "org.typelevel" % s"cats_${scalacMajor}" % "0.9.0"
    ,libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
)



