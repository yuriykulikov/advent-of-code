plugins {
  kotlin("jvm")
  jacoco
}

repositories { mavenCentral() }

dependencies {
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
  testImplementation("io.kotest:kotest-assertions-core:5.5.4")
  testImplementation("junit:junit:4.13.2")
}
