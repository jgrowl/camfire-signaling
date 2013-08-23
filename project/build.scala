import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import sbtassembly.Plugin._
import ScalateKeys._
import AssemblyKeys._

object CamfireSignalingBuild extends Build {
  val Organization = "tv.camfire"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.2"
  val ScalatraVersion = "2.2.1"
  val JettyVersion = "8.1.8.v20121106"
  val JSON4SJacksonVersion = "3.2.4"

  private val LicenseFile = """(license|licence|notice|copying)([.]\w+)?$""".r

  private def isLicenseFile(fileName: String): Boolean =
    fileName.toLowerCase match {
      case LicenseFile(_, ext) if ext != ".class" => true // DISLIKE
      case _ => false
    }

  private val ReadMe = """(readme)([.]\w+)?$""".r

  private def isReadme(fileName: String): Boolean =
    fileName.toLowerCase match {
      case ReadMe(_, ext) if ext != ".class" => true
      case _ => false
    }

  val defaultMergeStrategy: String => MergeStrategy = {
    case "reference.conf" | "rootdoc.txt" =>
      MergeStrategy.concat
    case PathList(ps@_*) if isReadme(ps.last) || isLicenseFile(ps.last) =>
      MergeStrategy.rename
    case PathList("META-INF", xs@_*) =>
      (xs map {
        _.toLowerCase
      }) match {
        case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
          MergeStrategy.discard
        case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
          MergeStrategy.discard
        case "plexus" :: xs =>
          MergeStrategy.discard
        case "services" :: xs =>
          MergeStrategy.filterDistinctLines
        case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
          MergeStrategy.filterDistinctLines
        case _ => MergeStrategy.deduplicate
      }
    //    case _ => MergeStrategy.deduplicate
    case _ => MergeStrategy.first
  }

  lazy val root = Project(
    id = "camfire-tv",
    base = file("."),
    settings = Defaults.defaultSettings ++ assemblySettings ++ Seq(
      organization := Organization,
      name := "camfire-tv",
      version := Version,
      scalaVersion := ScalaVersion
    )
  ) aggregate (sessionManagement, webrtcJacksonSerialization, signalingServer)

  val SessionManagementVersion = "0.1.0-SNAPSHOT"
  lazy val sessionManagement = Project(
    id = "session-management",
    base = file("session-management"),
    settings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := "session-management",
      version := SessionManagementVersion,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += Resolver.sonatypeRepo("releases"),
      libraryDependencies ++= Seq(
        "org.json4s" %% "json4s-jackson" % JSON4SJacksonVersion % "provided"
        ,"org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "provided"
        ,"commons-pool" %  "commons-pool" % "1.6"
        ,"org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "provided" artifacts (Artifact("javax.servlet", "jar", "jar"))
        ,"ch.qos.logback" % "logback-classic" % "1.0.6" % "provided"
      )
    )
  )

  val WebrtcJacksonSerializationVersion = "0.1.0-SNAPSHOT"
  lazy val webrtcJacksonSerialization = Project(
    id = "webrtc-jackson-serialization",
    base = file("webrtc-jackson-serialization"),
    settings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := "webrtc-jackson-serialization",
      version := WebrtcJacksonSerializationVersion,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += Resolver.sonatypeRepo("releases"),
      libraryDependencies ++= Seq(
        "org.json4s" %% "json4s-jackson" % JSON4SJacksonVersion % "provided",
        "org.specs2" %% "specs2" % "1.14" % "test"
      )
    )
  )

  val SignalingServerVersion = "0.1.0-SNAPSHOT"
  lazy val signalingServer = Project(
    id = "signaling-server",
    base = file("signaling-server"),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ assemblySettings ++ Seq(
      organization := Organization,
      name := "signaling-server",
      version := SignalingServerVersion,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += Resolver.sonatypeRepo("releases"),
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.0.2",
        "com.softwaremill.macwire" % "core_2.10" % "0.3",
        "org.scalatra" %% "scalatra-atmosphere" % "2.2.1",
        "org.scalatra" %% "scalatra-json" % "2.2.1",
        "org.json4s" %% "json4s-jackson" % JSON4SJacksonVersion,
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "rhino" % "js" % "1.7R2" % "test",
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.0" % "test",
        "com.typesafe.akka" %% "akka-testkit" % "2.1.2" % "test" intransitive(),
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-websocket" % JettyVersion % "container;compile",
        "org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "container;compile",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;compile" artifacts (Artifact("javax.servlet", "jar", "jar"))
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) {
        base =>
          Seq(
            TemplateConfig(
              base / "webapp" / "WEB-INF" / "templates",
              Seq.empty, /* default imports should be added here */
              Seq(
                Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
              ), /* add extra bindings here */
              Some("templates")
            )
          )
      }
      , mergeStrategy in assembly := defaultMergeStrategy
    )
  ) dependsOn(sessionManagement, webrtcJacksonSerialization)
}

