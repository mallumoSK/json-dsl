plugins {
    java
    kotlin("multiplatform") version "1.4.32"
    `maven-publish`
}

group = "tk.mallumo"
version = "1.0.0"



repositories {
    mavenCentral()
    maven("https://mallumo.jfrog.io/artifactory/gradle-dev-local")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvm()
    linuxX64()
    js{
        browser()
        nodejs()
    }
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting{
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
                implementation( "tk.mallumo:log:8.3.0")
                implementation("tk.mallumo:utils:12")
            }
        }
    }
}
apply("secure-json-dsl.gradle")