import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.example"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.7.20"
    val ebeanVersion = "13.10.0"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    id("io.ebean") version ebeanVersion
}

dependencies {
    val kotlinVersion = "1.7.20"
    val ebeanVersion = "13.10.0"

    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))

    implementation("io.ebean:ebean:$ebeanVersion")
    implementation("org.slf4j:slf4j-nop:2.0.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.1")
    testImplementation("io.ebean:ebean-test:$ebeanVersion")

    testRuntimeOnly("com.h2database:h2")

    kapt("io.ebean:kotlin-querybean-generator:$ebeanVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
