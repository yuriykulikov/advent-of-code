plugins {
  java
  jacoco
  id("org.jetbrains.kotlin.jvm")
}

dependencies {
  implementation(project(":collections"))
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
  testImplementation("junit:junit:4.12")
  testImplementation("io.vavr:vavr-kotlin:0.9.2")
}
