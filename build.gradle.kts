import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application

    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("maven-publish")
    id("signing")
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

fun Project.get(name: String, def: String = "$name not found") =
    properties[name]?.toString() ?: System.getenv(name) ?: def

publishing {
    publications {

        val props = project.properties

        repositories {
            maven {
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                // credentials are stored in ~/.gradle/gradle.properties with ~ being the path of the home directory
                credentials {
                    username = project.get("ossUsername")
                    password = project.get("ossPassword")
                }
            }
        }

        val publicationName = props["POM_NAME"]?.toString() ?: "publication"
        create<MavenPublication>(publicationName) {
            from(components["java"])

            pom {
                groupId = project.get("POM_GROUP_ID")
                artifactId = project.get("POM_ARTIFACT_ID")
                version = project.get("POM_VERSION_NAME")

                name.set(project.get("POM_NAME"))
                description.set(project.get("POM_DESCRIPTION"))
                url.set(project.get("POM_URL"))
                packaging = project.get("POM_PACKAGING")

                scm {
                    url.set(project.get("POM_SCM_URL"))
                    connection.set(project.get("POM_SCM_CONNECTION"))
                    developerConnection.set(project.get("POM_SCM_DEV_CONNECTION"))
                }

                organization {
                    name.set(project.get("POM_COMPANY_NAME"))
                    url.set(project.get("POM_COMPANY_URL"))
                }

                developers {
                    developer {
                        id.set(project.get("POM_DEVELOPER_ID"))
                        name.set(project.get("POM_DEVELOPER_NAME"))
                        email.set(project.get("POM_DEVELOPER_EMAIL"))
                    }
                }

                licenses {
                    license {
                        name.set(project.get("POM_LICENCE_NAME"))
                        url.set(project.get("POM_LICENCE_URL"))
                        distribution.set(project.get("POM_LICENCE_DIST"))
                    }
                }
            }
        }

        signing {
            sign(publishing.publications.getByName(publicationName))
        }
    }
}
