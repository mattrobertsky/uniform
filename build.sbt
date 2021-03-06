
scalaVersion := "2.11.12"
crossScalaVersions := Seq("2.11.12", "2.12.6")

organization := "uk.gov.hmrc"
name := "uniform"

homepage := Some(url("https://github.com/hmrclt/uniform"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/hmrclt/uniform"),
    "scm:git@github.com:hmrclt/uniform.git"
  )
)

developers := List(
  Developer(
    id            = "hmrclt",
    name          = "Luke Tebbs",
    email         = "luke.tebbs@digital.hmrc.gov.uk",
    url           = url("http://www.luketebbs.com/")
  )
)

libraryDependencies ++= {

  val playVersion = {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 11)) => "2.5.18"
      case _ => "2.6.16"
    }
  }

  Seq(
    "com.chuusai" %% "shapeless" % "2.3.3",
    "com.github.mpilquist" %% "simulacrum" % "0.12.0",
    "com.beachape" %% "enumeratum" % "1.5.13",
    "org.typelevel" %% "cats-core" % "1.1.0",
    "com.typesafe.play" %% "play" % playVersion % "provided"
  )
}

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

enablePlugins(GitVersioning)

git.gitTagToVersionNumber := { tag: String =>
  if(tag matches "[0-9]+\\..*") Some(tag)
  else None
}

initialCommands in console := """import uniform._"""

useGpg := true

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))
