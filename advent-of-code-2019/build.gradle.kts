plugins {
  java
  jacoco
  id("org.jetbrains.kotlin.jvm")
}

repositories {
  jcenter()
  maven(url = "https://kotlin.bintray.com/kotlinx/")
}

tasks.withType<Test> {
  // set heap size for the test JVM(s)
  minHeapSize = "4g"
  maxHeapSize = "8g"

  // set JVM arguments for the test JVM(s)
  // jvmArgs + = "-XX:MaxPermSize=256m"
}

dependencies {
  implementation(project(":collections"))
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.3")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
  implementation("io.reactivex.rxjava2:rxjava:2.2.16")

  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
  testImplementation("junit:junit:4.12")
  testImplementation("io.vavr:vavr-kotlin:0.9.2")
}
