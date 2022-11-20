import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "cz.sentica.qwazar"

plugins {
    val kotlinVersion = "1.7.21"
    val ebeanVersion = "13.10.1" // version "13.6.4" is the last for which this works

    id("java")
    // id("io.ebean") version ebeanVersion apply(false)

    kotlin("jvm") version kotlinVersion apply(false)
    kotlin("kapt") version kotlinVersion apply(false)
}

repositories {
    mavenCentral()
}

subprojects {
    val kotlinVersion = "1.7.21"
    val ebeanVersion = "13.10.1"

    apply(plugin = "java")
    apply(plugin = "kotlin")
    // apply(plugin = "kotlin-kapt") // can be enabled either here or in subproject, but if not all
    // apply(plugin = "io.ebean")    // subprojects use ebean, it's better to do it there

    java.sourceCompatibility = JavaVersion.VERSION_17

    repositories {
        mavenCentral()
    }

    ext["ebeanVersion"] = ebeanVersion

    dependencies {
        implementation(kotlin("stdlib", kotlinVersion))
        // implementation(kotlin("reflect", kotlinVersion))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
}
