plugins {
  base
  java
  jacoco
  `java-library`
  id("org.jetbrains.kotlin.jvm")
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
  implementation(project(":collections"))
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable-jvm:0.3")
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
  testImplementation("junit:junit:4.12")
  testImplementation("io.vavr:vavr-kotlin:0.9.2")
}
