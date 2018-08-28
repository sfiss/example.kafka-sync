import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories

group = "org.example"
version = "0.1-SNAPSHOT"


buildscript {
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    var kotlinVer: String by extra
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    var kotlinTestVer: String by extra

    @Suppress("UNUSED_VALUE")
    kotlinVer = "1.2.61"
    @Suppress("UNUSED_VALUE")
    kotlinTestVer = "2.0.7"
}

val kotlinVer: String by extra
val kotlinTestVer: String by extra

repositories {
    jcenter()
}

plugins {
    kotlin("jvm").version("1.2.61")
}

dependencies {
    compile(kotlin(module = "stdlib-jre8", version = kotlinVer))
    testCompile("io.kotlintest:kotlintest:$kotlinTestVer"")
}


