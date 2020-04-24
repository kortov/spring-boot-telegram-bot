import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
    jacoco
    id("info.solidsoft.pitest") version "1.4.0"
    id ("org.sonarqube") version "2.8"
}

group = "com.kortov"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

buildscript {
    configurations.maybeCreate("pitest")
    dependencies {
        "pitest"("org.pitest:pitest-junit5-plugin:0.9")
        "pitest"("org.pitest:pitest-kotlin-plugin:1.0")
    }
    repositories {
        maven(url = "https://dl.bintray.com/vantuz/pitest-kotlin")
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jcenter.bintray.com")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.github.microutils:kotlin-logging:1.5.9")
    implementation("org.telegram:telegrambots:4.7")
    implementation("org.telegram:telegrambots-abilities:4.7")
    implementation ( "org.apache.commons:commons-text:1.8")
    implementation ("org.jsoup:jsoup:1.12.1")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.beust:klaxon:5.2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        xml.destination = file("${buildDir}/reports/jacoco/report.xml")
        html.isEnabled = false
        csv.isEnabled = false
    }
}

plugins.withId("info.solidsoft.pitest") {
    configure<PitestPluginExtension> {
        testPlugin = "junit5"
        avoidCallsTo = setOf("kotlin.jvm.internal")
        mutationThreshold = 60
        pitestVersion = "1.4.9"
        threads = System.getenv("PITEST_THREADS")?.toInt() ?: Runtime.getRuntime().availableProcessors()
        outputFormats = setOf("XML", "HTML")
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "kortov_spring-boot-telegram-bot")
        property("sonar.organization", "kortov")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}