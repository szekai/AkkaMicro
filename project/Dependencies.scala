import sbt._

object Dependencies {
  val AkkaVersion = "2.6.18"
  val AkkaHttpVersion = "10.2.7"
  val TapirVersion = "0.20.0-M5"

  lazy val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  ).map(_.cross(CrossVersion.for3Use2_13))

  lazy val tapirDependencies = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % TapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion
  ).map(_.cross(CrossVersion.for3Use2_13))
}
