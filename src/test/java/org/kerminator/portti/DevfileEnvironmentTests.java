package org.kerminator.portti;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tests to verify the devfile environment configuration works correctly.
 * These tests check for the existence of required files and configurations.
 */
@SpringBootTest
class DevfileEnvironmentTests {

    @Test
    void devfileExists() {
        Path devfilePath = Paths.get(System.getProperty("user.dir")).resolve("devfile.yaml");
        assertTrue(Files.exists(devfilePath), "Devfile should exist in project root");
    }

    @Test
    void gradleWrapperExists() {
        Path gradlewPath = Paths.get(System.getProperty("user.dir")).resolve("gradlew");
        assertTrue(Files.exists(gradlewPath), "Gradle wrapper script should exist");
        
        // Check if the file is executable
        File gradlewFile = gradlewPath.toFile();
        assertTrue(gradlewFile.canExecute(), "Gradle wrapper script should be executable");
    }

    @Test
    void applicationStartsInDevfileEnvironment() {
        // This test will pass if the Spring context loads successfully
        // which verifies that the application can start in a devfile environment
        assertDoesNotThrow(() -> {
            // The SpringBootTest annotation already loads the context
            // so if we get here, it means the context loaded successfully
        });
    }

    /**
     * Test configuration that simulates a devfile environment.
     * This is activated when the "devfile" profile is active.
     */
    @TestConfiguration
    @Profile("devfile")
    static class DevfileTestConfiguration {
        
        @Bean
        public String devfileEnvironmentMarker() {
            // This bean is just a marker to indicate we're in a devfile environment
            return "devfile-environment";
        }
    }
}