plugins {
    val kotlinVersion = "1.9.25"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "3.5.12"
    id("io.spring.dependency-management") version "1.1.7"
//    kotlin("plugin.allopen") version kotlinVersion
    // 添加 Kotlin JPA 插件
    kotlin("plugin.jpa") version "1.9.0"
    // 或者使用 no-arg 插件
//    kotlin("plugin.noarg") version "1.9.0"
}

//allOpen {
//    // @Entity注解的类改为open 且所有属性改为open
//    annotation("jakarta.persistence.Entity")
//}

group = "com.darcy.kotlin.server"
version = "0.0.4-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
// Mockito Agent 注入
// https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
val mockitoAgent = configurations.create("mockitoAgent")

tasks {
    test {
        jvmArgs?.add("-javaagent:${mockitoAgent.asPath}")
    }
}

dependencies {
    val mockitoVersion = "5.23.0"
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    mockitoAgent("org.mockito:mockito-core:$mockitoVersion") { isTransitive = false }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // WebSocket 客户端测试依赖
    testImplementation("org.springframework.boot:spring-boot-starter-websocket")
//    testImplementation("org.java-websocket:Java-WebSocket:1.5.4")
    testImplementation ("org.springframework:spring-messaging")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("javax.websocket:javax.websocket-api:1.1")
    // database
    // java persistence api ORM框架-JPA(Hibernate实现)
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    // mysql
    implementation ("com.mysql:mysql-connector-j:9.4.0")
    // json
    implementation ("com.alibaba.fastjson2:fastjson2:2.0.61")
    // 添加 Spring Security 依赖(包含 BCryptPasswordEncoder)
    implementation("org.springframework.boot:spring-boot-starter-security")
    // 添加 JWT 依赖
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    implementation("io.jsonwebtoken:jjwt-impl:0.13.0")
    implementation("io.jsonwebtoken:jjwt-jackson:0.13.0")
    // 添加 Spring AOP 依赖
    implementation("org.springframework.boot:spring-boot-starter-aop")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
