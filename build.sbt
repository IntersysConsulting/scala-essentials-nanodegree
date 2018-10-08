import Dependencies._
import microsites._

enablePlugins(MicrositesPlugin)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.intersysconsulting",
      scalaVersion := "2.12.7",
      version      := "0.0.0-SNAPSHOT"
    )),
    name := "scala-essentials-nanodegree",
    micrositeName := "Scala Essentials",
    micrositeDescription      := "Scala Essentials Nanodegree",
    micrositeBaseUrl	      := "/scala-essentials-nanodegree",
    micrositeDocumentationUrl := "/scala-essentials-nanodegree/docs.html",
    micrositeStaticDirectory  := (resourceDirectory in Compile).value / "microsite" / "slides",
    micrositeAuthor           := "Intersys Consulting",
    micrositeGitterChannel    := true,
    micrositeGitterChannelUrl := "IntersysConsulting/scala-essentials-nanodegree",
    micrositeHomepage         := "https://intersysconsulting.github.io/scala-essentials-nanodegree",
    micrositeGithubOwner      := "intersysconsulting",
    micrositeGithubRepo       := "scala-essentials-nanodegree",
    micrositeHighlightLanguages ++= Seq("haskell", "fsharp", "scala", "python", "java"),
    micrositeCDNDirectives    := CdnDirectives(
      jsList = List(
        "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML,https://oscarvarto.github.io/learning-scala/js/mathjax-config.js"
      )
    ),
    libraryDependencies += scalaTest % Test
  )
