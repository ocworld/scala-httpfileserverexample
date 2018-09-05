lazy val root = (project in file("."))
  .settings(
    name := "httpfileserverexample",
    version := "0.1",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      :
      "com.twitter" %% "finagle-http" % "18.8.0"
    )
  )

