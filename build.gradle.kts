import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application

    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("maven-publish")
}

group = "com.flagsmith"
version = "5.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))

    // https://mvnrepository.com/artifact/com.flagsmith/flagsmith-java-client
    api("com.flagsmith:flagsmith-java-client:5.0.0") {
        isTransitive = true
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        // Specify relocation POM
        create<MavenPublication>("mavenJava") {
            pom {
                // Old artifact coordinates
                groupId = "com.flagsmith"
                artifactId = "flagsmith-kotlin-client"
                version = "1.0.0"

                from(components["java"])
            }
        }
    }
}
