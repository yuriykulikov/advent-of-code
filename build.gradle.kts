// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  java
  val kotlin = "1.9.0"
  kotlin("jvm") version kotlin apply false
  id("com.diffplug.spotless") version "6.11.0"
  id("com.adarshr.test-logger") version "3.2.0"
}

allprojects { repositories { mavenCentral() } }

subprojects {
  apply(plugin = "com.adarshr.test-logger")
  testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
    showPassed = false
    showSkipped = true
    showFailed = true
  }
}

tasks.clean { delete(rootProject.buildDir) }

apply(plugin = "com.diffplug.spotless")

spotless {
  kotlin {
    target("build.gradle.kts")
    ktfmt()
    lineEndings = com.diffplug.spotless.LineEnding.UNIX
  }
}
