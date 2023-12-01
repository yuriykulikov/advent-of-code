plugins {
  kotlin("jvm")
  id("com.diffplug.spotless")
}

repositories { mavenCentral() }

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testImplementation("io.kotest:kotest-assertions-core:5.5.4")
}

tasks.test { useJUnitPlatform() }
