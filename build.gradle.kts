import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

group = "cz.sentica.qwazar"

plugins {
    val kotlinVersion = "1.9.22"
    val ebeanVersion = "13.25.1"

    id("java")
    id("io.ebean") version ebeanVersion apply(false)
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3" apply (false)

    kotlin("jvm") version kotlinVersion apply(false)
    kotlin("kapt") version kotlinVersion apply(false)
}

repositories {
    mavenCentral()
}

subprojects {
    val kotlinVersion = "1.9.22"
    val ebeanVersion = "13.25.1"

    apply(plugin = "java")
    apply(plugin = "kotlin")
    // apply(plugin = "kotlin-kapt") // can be enabled either here or in subproject, but if not all
    // apply(plugin = "io.ebean")    // subprojects use ebean, it's better to do it there
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

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

    configure<KtlintExtension> {
        version.set("1.1.1")
        verbose.set(true)
        // debug.set(true)
    }
}
