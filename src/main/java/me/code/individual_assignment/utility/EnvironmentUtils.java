package me.code.individual_assignment.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnvironmentUtils {

    @Autowired
    private Environment environment;

    public boolean isTestEnvironment() {
        // Check if the "test" profile is active
        return Arrays.asList(environment.getActiveProfiles()).contains("test");
    }
}
