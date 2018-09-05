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
    var spring_boot_version: String by extra
    spring_boot_version = "2.0.1.RELEASE"
    var kafka_version: String by extra
    kafka_version = "2.0.0"

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.spring.io/milestone")
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

val kotlin_version: String by extra
val spring_boot_version: String by extra
val kafka_version: String by extra

plugins {
    java
    kotlin("jvm") version "1.2.61"
    kotlin("plugin.spring") version "1.2.61"
    kotlin("plugin.jpa") version "1.2.61"

    id("org.springframework.boot") version "2.0.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.4.RELEASE"
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlin_version))
    compile("org.springframework.boot:spring-boot-starter-web:$spring_boot_version")
    compile("org.springframework.boot:spring-boot-starter-data-jpa:$spring_boot_version")
    compile("org.springframework.kafka:spring-kafka:2.1.4.RELEASE")
    compile(kotlin("reflect", kotlin_version))
    compile("org.apache.kafka:kafka-streams:$kafka_version")
    compile("org.apache.kafka:kafka-clients:$kafka_version")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")

    runtime("org.postgresql:postgresql")
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