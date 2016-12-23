lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val lens = (project in file("flow-lens"))
  .settings(commonSettings: _*)
  .settings(
    name := "flow-lens",
    version := "1.0",
    libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % "2.11.8")
  )

lazy val examples = (project in file("flow-examples"))
  .dependsOn(lens)
  .settings(commonSettings: _*)
  .settings(
    name := "flow-examples",
    version := "1.0"
  )

lazy val root = (project in file("."))
  .aggregate(lens, examples)
  .settings(commonSettings: _*)
  .settings(
    name := "flow-mvp",
    version := "1.0"
  )
