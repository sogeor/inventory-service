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
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    compileOnly("org.jetbrains:annotations:${findProperty("org.jetbrains.annotations.version")}")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${
        findProperty("org.springdoc.springdoc-openapi-starter-webmvc-ui.version")
    }")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor(
        "org.projectlombok:lombok-mapstruct-binding:${findProperty("org.projectlombok.lombok-mapstruct-binding")}")

    implementation("com.zaxxer:HikariCP:${findProperty("com.zaxxer.HikariCP")}")
    implementation("org.mapstruct:mapstruct:${findProperty("org.mapstruct.mapstruct")}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${findProperty("org.mapstruct.mapstruct")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom(
            "org.springframework.cloud:spring-cloud-dependencies:${findProperty("org.springframework.cloud.version")}")
    }
}

tasks.wrapper {
    gradleVersion = "9.2.1"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
