import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

group = "ru.itmo"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.jcraft:jsch:0.1.55")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.8.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.slf4j:jcl-over-slf4j:2.0.9")

    //implementation("org.slf4j:slf4j-api:1.7.32")
    //implementation("ch.qos.logback:logback-classic:1.2.9")
    //implementation("ch.qos.logback:logback-core:1.2.9")

    runtimeOnly("tech.ydb.jdbc:ydb-jdbc-driver:2.0.3")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    enabled = false
}