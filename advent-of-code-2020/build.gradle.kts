plugins {
  java
  id("org.jetbrains.kotlin.jvm")
}

repositories { mavenCentral() }

tasks.withType<Test> {
  // set heap size for the test JVM(s)
  // minHeapSize = "4g"
  // maxHeapSize = "8g"

  // set JVM arguments for the test JVM(s)
  // jvmArgs + = "-XX:MaxPermSize=256m"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions { jvmTarget = "1.8" }
}

dependencies {
  implementation(project(":collections"))
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.3")
  implementation("io.reactivex.rxjava2:rxjava:2.2.16")
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
  testImplementation("junit:junit:4.12")
}
