package me.code.individual_assignment.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The EnvironmentUtils class provides utility methods related to the application's environment.
 */
@Component
public class EnvironmentUtils {

    @Autowired
    private Environment environment;

    /**
     * Checks if the application is running in a test environment.
     *
     * @return true if the "test" profile is active, false otherwise.
     */
    public boolean isTestEnvironment() {
        // Check if the "test" profile is active.
        return Arrays.asList(environment.getActiveProfiles()).contains("test");
    }
}
