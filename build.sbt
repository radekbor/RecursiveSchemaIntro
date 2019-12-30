name := "recursive-introduction-article"

version := "0.1"

scalaVersion := "2.12.0"

libraryDependencies += "com.slamdata" %% "matryoshka-core" % "0.21.3"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.27"

scalacOptions += "-Ypartial-unification"