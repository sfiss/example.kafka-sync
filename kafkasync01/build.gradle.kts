import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.serialization.builtins.main

group = "org.example"
version = "0.1-SNAPSHOT"

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.61"
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

val kotlin_version: String by extra

plugins {
    java
    kotlin("jvm") version "1.2.61"
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlin_version")
}

repositories {
    mavenCentral()
}

java.sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}