
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "AkkaMicroScala2",
    libraryDependencies ++=
      Dependencies.akkaDependencies ++
      Dependencies.tapirDependencies,
  ).enablePlugins(JavaServerAppPackaging)
