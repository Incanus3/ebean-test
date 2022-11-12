group = "cz.sentica.qwazar.ea"

plugins {
    kotlin("kapt")
    id("io.ebean")
}

dependencies {
    api(project(":ea:core"))
    api("io.ebean:ebean:${project.ext["ebeanVersion"]}")

    implementation("org.slf4j:slf4j-nop:2.0.3")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.3.2")
    testImplementation("io.ebean:ebean-test:${project.ext["ebeanVersion"]}")

    testRuntimeOnly("com.h2database:h2")

    kapt("io.ebean:kotlin-querybean-generator:${project.ext["ebeanVersion"]}")
}

tasks.test {
    useJUnitPlatform()
}
