import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.example"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.7.21"
    val ebeanVersion = "13.10.1"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    id("io.ebean") version ebeanVersion
}

dependencies {
    val kotlinVersion = "1.7.21"
    val ebeanVersion = "13.10.1"
    val kotestVersion = "5.5.4"

    implementation(kotlin("stdlib", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))

    implementation("io.ebean:ebean:$ebeanVersion")
    implementation("org.slf4j:slf4j-nop:2.0.3")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.ebean:ebean-test:$ebeanVersion")

    testRuntimeOnly("com.h2database:h2")

    kapt("io.ebean:kotlin-querybean-generator:$ebeanVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
        jvmTarget = "17"
    }
}
