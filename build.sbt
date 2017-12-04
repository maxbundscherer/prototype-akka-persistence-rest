/**
  * Root
  */
lazy val appRoot = project.in(file("."))

  .settings(DefaultCommons.Settings.getDefaultSettings(myName = "Prototype-Akka-Persistence-Rest", myVersion = "0.0.1"))
  .settings( libraryDependencies ++= DefaultCommons.Dependencies.getDefaultDependencies )
  /**
    * Docker image
    */
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf"))