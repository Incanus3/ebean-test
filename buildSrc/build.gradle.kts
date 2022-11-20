group = "io.ebean"
version = "13.10.1"

plugins {
  id("groovy")
  id("java-gradle-plugin")
}

repositories {
  // mavenLocal()
  mavenCentral()
}

dependencies {
  implementation("io.ebean:ebean-agent:13.10.1")
  implementation(gradleApi())
  implementation(localGroovy())
}
