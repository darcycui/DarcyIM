plugins {
    val kotlinVersion = "1.9.25"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
//    kotlin("plugin.allopen") version kotlinVersion
}

group = "com.darcy.kotlin.server"
version = "0.0.4-SNAPSHOT"

//allOpen {
//    // @Entity注解的类改为open 且所有属性改为open
//    annotation("jakarta.persistence.Entity")
//}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(21)
//    }
//    sourceCompatibility = JavaVersion.VERSION_21
//    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("javax.websocket:javax.websocket-api:1.1")
    // database
    // java persistence api ORM框架
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    // mysql
    implementation ("mysql:mysql-connector-java:8.0.33")
    // json
    implementation ("com.alibaba:fastjson:2.0.51")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
