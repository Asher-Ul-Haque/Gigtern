plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.2.21"
}

group = "just.somebody"
version = "0.0.1-SNAPSHOT"
description = "Gigtern Backend"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

tasks.bootJar {
	archiveFileName.set("app.jar")
}


repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa");
	implementation("org.springframework.boot:spring-boot-starter-web");
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin");
	implementation("org.jetbrains.kotlin:kotlin-reflect");
	implementation("org.springframework.boot:spring-boot-starter-validation");
	implementation("org.springframework.boot:spring-boot-starter-security");
	testImplementation("org.springframework.security:spring-security-test")
	implementation("io.jsonwebtoken:jjwt-api:0.12.5");
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5");
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5");
	runtimeOnly("org.postgresql:postgresql");
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-restclient-test")
	testImplementation("com.h2database:h2");
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5");
	testRuntimeOnly("org.junit.platform:junit-platform-launcher");
}


kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
