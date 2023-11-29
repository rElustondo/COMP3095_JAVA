plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation(enforcedPlatform("org.testcontainers:testcontainers-bom:1.16.2"))
	implementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:junit-jupiter") // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webflux
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.5")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3")
	// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-circuitbreaker-resilience4j
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.0.3")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
