import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.intellij") version "1.13.2"
    kotlin("kapt") version "1.6.10"
}

group = "boilerplatecoder"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("com.google.dagger:dagger:2.41")
    kapt("com.google.dagger:dagger-compiler:2.41")
}

configurations {
    compileClasspath.get().resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEPENDENCY_FIRST)
    testCompileClasspath.get().resolutionStrategy.sortArtifacts(ResolutionStrategy.SortOrder.DEPENDENCY_FIRST)
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.5")
//    version.set("2022.1.4")
//    type.set("IC") // Target IDE Platform
    plugins.set(listOf("org.jetbrains.kotlin"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

//      runIde {
//        ideDir.set(file("/Users/akaneiro/Applications/Android Studio.app/Contents"))
//    }

  patchPluginXml {
    sinceBuild.set("222")
    untilBuild.set("232.*")
  }

//  signPlugin {
//    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
//    privateKey.set(System.getenv("PRIVATE_KEY"))
//    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
//  }

//    runPluginVerifier {
//        ideVersions.set(listOf("IU-182.5262.2"))
//    }

//  publishPlugin {
//    token.set(System.getenv("PUBLISH_TOKEN"))
//  }

}
