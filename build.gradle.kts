plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.springframework.boot:spring-boot-starter-web") // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-security") // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-security") // Encoded Password
    runtimeOnly("com.h2database:h2") // H2 Database for testing
    runtimeOnly("org.postgresql:postgresql") // PostgreSQL
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Test
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.test {
    useJUnitPlatform()
}