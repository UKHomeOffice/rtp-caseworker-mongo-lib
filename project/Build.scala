import io.gatling.sbt.GatlingPlugin
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin._

object Build extends Build {
  val moduleName = "rtp-caseworker-mongo-lib"

  lazy val caseworkerMongo = Project(id = moduleName, base = file(".")).enablePlugins(GatlingPlugin)
    .configs(IntegrationTest)
    .settings(Revolver.settings)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.1.0",
      scalaVersion := "2.11.7",
      scalacOptions ++= Seq(
        "-feature",
        "-language:implicitConversions",
        "-language:higherKinds",
        "-language:existentials",
        "-language:reflectiveCalls",
        "-language:postfixOps",
        "-Yrangepos",
        "-Yrepl-sync"
      ),
      ivyScala := ivyScala.value map {
        _.copy(overrideScalaVersion = true)
      },
      resolvers ++= Seq(
        "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"
      ),
      libraryDependencies ++= Seq(
      ),
      libraryDependencies ++= Seq(
        "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7" % IntegrationTest withSources(),
        "io.gatling" % "gatling-test-framework" % "2.1.7" % IntegrationTest withSources()
      )
    )
    //.settings(javaOptions += "-Dconfig.resource=application.no.pollers.conf")
    .settings(run := (run in Runtime).evaluated) // Required to stop Gatling plugin overriding the default "run".

  val testPath = "../rtp-test-lib"
  val mongoPath = "../rtp-mongo-lib"

  val root = if (file(testPath).exists && sys.props.get("jenkins").isEmpty) {
    println("=============")
    println("Build Locally")
    println("=============")

    val test = ProjectRef(file(testPath), "rtp-test-lib")
    val mongo = ProjectRef(file(mongoPath), "rtp-mongo-lib")

    caseworkerMongo
      .dependsOn(test % "test->test;compile->compile")
      .dependsOn(mongo % "test->test;compile->compile")
  } else {
    println("================")
    println("Build on Jenkins")
    println("================")

    caseworkerMongo.settings(
      libraryDependencies ++= Seq(
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.2.0-SNAPSHOT" withSources(),
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.2.0-SNAPSHOT" % Test classifier "tests" withSources(),
        "uk.gov.homeoffice" %% "rtp-mongo-lib" % "1.1.0-SNAPSHOT" withSources(),
        "uk.gov.homeoffice" %% "rtp-mongo-lib" % "1.1.0-SNAPSHOT" % Test classifier "tests" withSources()
      )
    )
  }
}