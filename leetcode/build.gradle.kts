plugins {
  kotlin("jvm")
  jacoco
}

repositories { mavenCentral() }

dependencies {
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
  testImplementation("junit:junit:4.12")
}
