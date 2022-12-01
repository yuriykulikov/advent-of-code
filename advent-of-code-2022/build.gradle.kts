plugins {
  kotlin("jvm")
  id("com.diffplug.spotless")
}

dependencies { implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5") }

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "11" }

tasks { test { useJUnitPlatform() } }

spotless {
  kotlin {
    target("build.gradle.kts", "**/*.kt")
    ktfmt()
    lineEndings = com.diffplug.spotless.LineEnding.UNIX
  }
}
