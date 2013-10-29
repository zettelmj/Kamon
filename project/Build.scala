import sbt._
import Keys._

object Build extends Build {
  import AspectJ._
  import NewRelic._
  import Settings._
  import Dependencies._



  lazy val root = Project("root", file("."))
    .aggregate(kamonCore, kamonTrace, kamonMetrics, kamonSpray, kamonNewrelic, kamonPlayground, kamonDashboard)
    .settings(basicSettings: _*)
    .settings(noPublishing: _*)

  lazy val kamonCore = Project("kamon-core", file("kamon-core"))
    .settings(basicSettings: _*)
    .settings(aspectJSettings: _*)
    .settings(
      libraryDependencies ++=
        compile(akkaActor, aspectJ, metrics) ++
        test(scalatest, akkaTestKit))


  lazy val kamonTrace = Project("kamon-trace", file("kamon-trace"))
    .settings(basicSettings: _*)
    .settings(aspectJSettings: _*)
    .settings(
      libraryDependencies ++=
        compile(akkaActor, aspectJ) ++
        test(scalatest, akkaTestKit, sprayTestkit))

  lazy val kamonMetrics = Project("kamon-metrics", file("kamon-metrics"))
    .settings(basicSettings: _*)
    .settings(aspectJSettings: _*)
    .settings(
    libraryDependencies ++=
      compile(akkaActor, aspectJ, newrelic) ++
      test(scalatest, akkaTestKit, sprayTestkit))


  lazy val kamonSpray = Project("kamon-spray", file("kamon-spray"))
    .settings(basicSettings: _*)
    .settings(aspectJSettings: _*)
    .settings(
      libraryDependencies ++=
        compile(akkaActor, aspectJ, sprayCan, sprayClient, sprayRouting) ++
        test(scalatest, akkaTestKit, sprayTestkit))


  lazy val kamonNewrelic = Project("kamon-newrelic", file("kamon-newrelic"))
    .settings(basicSettings: _*)
    .settings(aspectJSettings: _*)
    .settings(
      libraryDependencies ++=
        compile(aspectJ, sprayCan, sprayClient, sprayRouting, newrelic) ++
        test(scalatest, akkaTestKit, sprayTestkit))

  lazy val kamonPlayground = Project("kamon-playground", file("kamon-playground"))
    .settings(basicSettings: _*)
    .settings(revolverSettings: _*)
    .settings(newrelicSettings: _*)
    .settings(noPublishing: _*)
    .settings(
      libraryDependencies ++=
        compile(akkaActor, akkaSlf4j, sprayCan, sprayClient, sprayRouting, logback))
    .dependsOn(kamonCore)



  lazy val kamonDashboard = Project("kamon-dashboard", file("kamon-dashboard"))
    .settings(basicSettings: _*)
    .settings(libraryDependencies ++= compile(akkaActor, akkaSlf4j, sprayRouting, sprayCan, sprayJson))
    .dependsOn(kamonCore)


  val noPublishing = Seq(publish := (), publishLocal := ())
}