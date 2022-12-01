plugins {
  java
  jacoco
  `java-library`
  id("org.jetbrains.kotlin.jvm")
}

dependencies {
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.1.1")
  testImplementation("junit:junit:4.12")
  testImplementation("org.mockito:mockito-core:2.8.47")
  testImplementation("io.vavr:vavr-kotlin:0.9.2")
}

tasks.jacocoTestReport {
  reports {
    xml.isEnabled = true
    csv.isEnabled = false
    html.destination = file("$buildDir/jacocoHtml")
  }
}

// val sourcesJar = tasks.create<Jar>("sourcesJar") {
//     from(sourceSets.main.get().allSource)
//     classifier = "sources"
// }
//
// val dokka = tasks.named<DokkaTask>("dokka") {
//     outputFormat = "javadoc"
//     outputDirectory = "$buildDir/javadoc"
// }
//
// val javadocJar = tasks.create<Jar>("javadocJar") {
//     dependsOn(dokka)
//     from(dokka)
//     classifier = "javadoc"
// }
//
// publishing {
//     publications {
//         create("Development", MavenPublication::class) {
//             from(components.findByName("java"))
//             artifact(sourcesJar)
//             artifact(javadocJar)
//             groupId = "${rootProject.group}"
//             artifactId = "kotlin-extensions-collections"
//             version = "${rootProject.version}"
//         }
//     }
// }
//
// bintray {
//     user = rootProject.properties.getOrDefault("bintrayUser", System.getenv("BINTRAY_USER") ?:
// "") as String
//     key = rootProject.properties.getOrDefault("bintrayApiKey", System.getenv("BINTRAY_API_KEY")
// ?: "") as String
//     setPublications("Development")
//     pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
//         repo = "kotlin-extensions"
//         name = "kotlin-extensions-collections"
//         issueTrackerUrl = "https://github.com/yuriykulikov/kotlin-extensions/issues"
//         setLicenses("MIT")
//         vcsUrl = "https://github.com/yuriykulikov/kotlin-extensions.git"
//         description = "Extension functions for Kotlin Collections"
//         override = true
//         publish = true
//         version(delegateClosureOf<BintrayExtension.VersionConfig> {
//             name = "${rootProject.version}"
//             desc = "Kotlin extensions kotlin-extensions-collections ${rootProject.version}"
//             released = "${Date()}"
//             vcsTag = "${rootProject.version}"
//         })
//     })
// }
