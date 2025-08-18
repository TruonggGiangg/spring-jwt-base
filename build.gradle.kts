plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}



repositories {
	mavenCentral()
}

dependencies {	
	implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") 
    implementation("mysql:mysql-connector-java:8.0.33") 
	implementation("org.springframework.boot:spring-boot-starter-security")

	// https://mvnrepository.com/artifact/org.projectlombok/lombok
	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	  // Validation (để Spring quản lý phiên bản, bỏ số version)
    implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}