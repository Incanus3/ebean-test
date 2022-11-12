group = "cz.sentica.qwazar.ea"

plugins {
    kotlin("kapt")
    id("io.ebean")
}

dependencies {
    api("io.ebean:ebean-api:${project.ext["ebeanVersion"]}")
    api("io.ebean:ebean-querybean:${project.ext["ebeanVersion"]}")

    kapt("io.ebean:kotlin-querybean-generator:${project.ext["ebeanVersion"]}")
}

tasks.test {
    useJUnitPlatform()
}
