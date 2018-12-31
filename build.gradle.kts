// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    java
}

extra.set("kotlinVersion", "1.4.20")

buildscript {
    val kotlinVersion by rootProject.extra { "1.4.20" }
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

tasks.clean {
    delete(rootProject.buildDir)
}
