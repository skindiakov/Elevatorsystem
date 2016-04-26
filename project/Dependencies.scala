import sbt._

object Dependencies {
  // Versions
  val scalatraVersion    = "2.4.0"
  val servletVersion     = "3.1.0"
  val akkaVersion        = "2.4.2"
  val json4sVersion      = "3.3.0"
  val scalajHttpVersion  = "2.2.1"
  val casbahVersion      = "3.1.1"
  val scalatestVersion   = "2.2.6"
  val mockitoVersion     = "1.10.19"
  val logbackVersion     = "1.1.6"
  val slf4jVersion       = "1.7.18"
  val configVersion      = "1.3.0"
  val jettyVersion       = "9.2.15.v20160210"


  // Libraries
  val scalatra = Seq(
    "org.scalatra"       %% "scalatra"             % scalatraVersion exclude("org.slf4j", "log4j12"),
    "org.scalatra"       %% "scalatra-json"        % scalatraVersion,
    "org.scalatra"       %% "scalatra-swagger"     % scalatraVersion,
    "org.scalatra"       %% "scalatra-scalatest"   % scalatraVersion    % "it,test")

  val servletApi =
    "javax.servlet"      %  "javax.servlet-api"    % servletVersion     % "provided"

  val akka = Seq(
    "com.typesafe.akka"  %% "akka-actor"           % akkaVersion,
    "com.typesafe.akka"  %% "akka-testkit"         % akkaVersion        % "it,test")

  val json4s =
    "org.json4s"         %% "json4s-jackson"       % json4sVersion

  val httpClient =
    "org.scalaj"         %% "scalaj-http"          % scalajHttpVersion

  val casbah =
    "org.mongodb"        %% "casbah"               % casbahVersion

  val scalatest =
    "org.scalatest"      %% "scalatest"            % scalatestVersion   % "it,test"

  val mockito =
    "org.mockito"        %  "mockito-core"         % mockitoVersion     % "it,test"

  val logback =
    "ch.qos.logback"     %  "logback-classic"      % logbackVersion     % "runtime"

  val slf4j =
    "org.slf4j"          %  "slf4j-api"            % slf4jVersion       % "provided"

  val typesafeConfig =
    "com.typesafe"       % "config"                % configVersion

  val jetty =
    "org.eclipse.jetty"  %  "jetty-webapp"         % jettyVersion       % "compile"

  val db =  Seq( "com.h2database" % "h2" % "1.4.185" % "compile",
    "org.scalikejdbc" %% "scalikejdbc" % "2.3.5")


  val commonDepsPack = Seq(
    logback,
    scalatest,
    mockito,
    typesafeConfig
  )

  // Projects
  val utilsDeps = commonDepsPack :+ slf4j

  val dataDeps = commonDepsPack ++ Seq(httpClient, casbah, json4s, slf4j)

  val processingDeps = commonDepsPack ++ akka

  val restDeps = commonDepsPack ++ scalatra ++ Seq(json4s, jetty, servletApi)

  val allDeps = utilsDeps ++ dataDeps ++ processingDeps ++ restDeps ++ db
}