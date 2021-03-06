
val moduleName = "rtp-caseworker-mongo-lib"

val root = Project(id = moduleName, base = file("."))
  .enablePlugins(GitVersioning)
  .configs(IntegrationTest)
  .settings(
    name := moduleName,
    organization := "uk.gov.homeoffice",
    scalaVersion := "2.12.6",
    crossScalaVersions := Seq("2.11.8", "2.12.6")
  )

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers ++= Seq(
  "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
  "Artifactory Release Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-release-local/",
  "Artifactory External Release Local Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/ext-release-local/"
)

libraryDependencies ++= Seq(
  "uk.gov.homeoffice" %% "rtp-test-lib" % "1.6.6-g6f56307",
  "uk.gov.homeoffice" %% "rtp-mongo-lib" % "3.2.0",
  "uk.gov.homeoffice" %% "rtp-mongo-lib" % "3.2.0" % Test classifier "tests"
)

publishTo := {
  val artifactory = sys.env.get("ARTIFACTORY_SERVER").getOrElse("http://artifactory.registered-traveller.homeoffice.gov.uk/")
  Some("release"  at artifactory + "artifactory/libs-release-local")
}

publishArtifact in (Test, packageBin) := true
publishArtifact in (Test, packageDoc) := true
publishArtifact in (Test, packageSrc) := true

git.useGitDescribe := true
git.gitDescribePatterns := Seq("v?.?")
git.gitTagToVersionNumber := { tag :String =>

val branchTag = if (git.gitCurrentBranch.value == "master") "" else "-" + git.gitCurrentBranch.value
val uncommit = if (git.gitUncommittedChanges.value) "-U" else ""

tag match {
  case v if v.matches("v\\d+.\\d+") => Some(s"$v.0${uncommit}".drop(1))
  case v if v.matches("v\\d+.\\d+-.*") => Some(s"${v.replaceFirst("-",".")}${branchTag}${uncommit}".drop(1))
  case _ => None
}}
