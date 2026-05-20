plugins {
    id("java")
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "7.3.0.8198"
}

group = "org.kerminator"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val developmentOnly by configurations
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

/* jib {
	from {
		platforms {
    		platform {
				architecture = "amd64"
				os = "linux"
    		}
			platform {
				architecture = "arm64"
				os = "linux"
			}
    	}
	}
		
	to {
		image = "public.ecr.aws/kerminator/spring-portti:latest"
		credHelper {
			helper = "ecr-login"
		}
	}
} */

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    imageName = "public.ecr.aws/kerminator/spring-${project.name}"
    createdDate = "now"
    environment.put("BP_JVM_VERSION", "25")
    //environment.put("BP_JVM_JLINK_ENABLED", "true")
    //environment.put("BP_JVM_CDS_ENABLED", "true")
    //environment.put("BP_SPRING_AOT_ENABLED", "true")
    //environment.put("BP_NATIVE_IMAGE", "true")
}

val springCloudVersion by extra("2025.1.0")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
    implementation("org.springframework.cloud:spring-cloud-starter-kubernetes-client-all")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.4")
    implementation("org.springframework.boot:spring-boot-micrometer-tracing")
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testImplementation("org.springframework.boot:spring-boot-micrometer-tracing-test")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-opentelemetry-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    systemProperties(
        mapOf(
            "junit.jupiter.execution.parallel.enabled" to "true"
        )
    )
}

sonarqube {
    properties {
        property("sonar.projectKey", "spring-portti")
        property("sonar.organization", "jahpola")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.tests", "src/test/java")
    }
}