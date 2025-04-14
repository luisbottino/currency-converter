plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "1.9.10"
}

group = "br.com.luisbottino"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //DB
    runtimeOnly("com.h2database:h2")

    //Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}