group = "cz.sentica.qwazar.ea"

plugins {
    kotlin("kapt")
    id("io.ebean")
    id("java-test-fixtures")
}

dependencies {
    api(project(":ea:core"))
    api("io.ebean:ebean:${project.ext["ebeanVersion"]}")

    implementation("org.slf4j:slf4j-api:2.0.3")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    testImplementation(kotlin("test"))
    testImplementation("io.ebean:ebean-test:${project.ext["ebeanVersion"]}")

    testRuntimeOnly("com.h2database:h2:2.2.220")

    kapt("io.ebean:kotlin-querybean-generator:${project.ext["ebeanVersion"]}")
    kaptTest("io.ebean:kotlin-querybean-generator:${project.ext["ebeanVersion"]}")
}

ebean {
    debugLevel = 2
}

tasks.test {
    useJUnitPlatform()
}
