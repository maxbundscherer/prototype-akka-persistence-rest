import sbt._
import Keys._

object DefaultCommons {

  object Settings {

    private val myOrganization = "com.mb"
    private val myScalaVersion = "2.12.4"

    def getDefaultSettings(myName: String, myVersion: String): Seq[Def.Setting[_]] = Seq (

      organization  := myOrganization,
      scalaVersion  := myScalaVersion,
      name          := myName,
      version       := myVersion

    )

  }

  object Dependencies {

    private val scalaTestVersion: String        = "3.0.4"
    private val typeSafeConfigVersion: String   = "1.3.1"
    private val akkaVersion: String             = "2.4.17"
    private val akkaHttpVersion: String         = "10.0.11"
    private val akkaHttpCirceVersion: String    = "1.18.0"
    private val circeGenericVersion: String     = "0.8.0"
    private val persistenceRedisVersion: String = "0.7.0"

    private val scalactic         = "org.scalactic" %% "scalactic" % scalaTestVersion
    private val scalatest         = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    private val typesafe          = "com.typesafe" % "config" % typeSafeConfigVersion
    private val akkaPersistence   = "com.typesafe.akka" %% "akka-persistence" % akkaVersion
    private val akkaHttp          = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    private val akkaHttpTest      = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
    private val akkaHttpCirce     = "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion
    private val circeGeneric      = "io.circe" %% "circe-generic" % circeGenericVersion
    private val persistenceRedis  = "com.hootsuite" %% "akka-persistence-redis" % persistenceRedisVersion

    def getDefaultDependencies: Seq[ModuleID] = Seq (

      scalactic,
      scalatest,
      typesafe,
      akkaPersistence,
      akkaHttp,
      akkaHttpTest,
      akkaHttpCirce,
      circeGeneric,
      persistenceRedis

    )

  }

}