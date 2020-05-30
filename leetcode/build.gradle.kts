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
    val kotlinVersion = rootProject.extra.get("kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
    testImplementation("junit:junit:4.12")
}
