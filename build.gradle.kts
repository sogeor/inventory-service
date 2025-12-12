plugins {
    java
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.sogeor"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    maven("https://nexus.sogeor.com/repository/maven-public/") {
        credentials {
            username = findProperty("SOGEOR_NEXUS_USERNAME")?.toString() ?: System.getenv("NEXUS_USERNAME")
            password = findProperty("SOGEOR_NEXUS_PASSWORD")?.toString() ?: System.getenv("NEXUS_PASSWORD")
        }
    }
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-kafka")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("org.jetbrains:annotations:${property("o.jetbrains.annotations")}")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api:${
        property("o.springdoc.springdoc-openapi-starter-webflux-api")
    }")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${
        property("o.projectlombok.lombok-mapstruct-binding")
    }")

    implementation("com.zaxxer:HikariCP:${property("c.zaxxer.HikariCP")}")
    implementation("org.mapstruct:mapstruct:${property("o.mapstruct.mapstruct")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${property("o.mapstruct.mapstruct")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("o.s.cloud")}")
    }
}

tasks.wrapper {
    gradleVersion = "9.2.1"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
