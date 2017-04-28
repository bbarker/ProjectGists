name := "SparkSessionTester"

version := "1.0"

lazy val scalaVersionMajor = "2.11"
lazy val scalaVersionMinor = "11"
scalaVersion := s"$scalaVersionMajor.$scalaVersionMinor"

lazy val sparkVersion = "2.1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "com.holdenkarau" %% "spark-testing-base" % "0.6.0",
  "log4j" % "log4j" % "1.2.17",
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "junit" % "junit" % "4.12" % Test
)

        