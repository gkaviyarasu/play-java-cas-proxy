name := """corridor-common-service"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.37",
  "junit" % "junit" % "4.11",
  "org.jasig.cas.client" % "cas-client-core" % "3.4.1",
  //"org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "io.swagger" %% "swagger-play2" % "1.5.0"//,
  //"org.pac4j" % "play-pac4j-java" % "2.0.1",
  //"org.pac4j" % "pac4j-cas" % "1.8.5"
)
/*
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
resolvers ++= Seq( Resolver.mavenLocal,
  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Pablo repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/")
*/

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
//routesGenerator := StaticRoutesGenerator
