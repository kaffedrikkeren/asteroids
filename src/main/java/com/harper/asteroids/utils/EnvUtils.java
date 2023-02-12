package com.harper.asteroids.utils;

import java.util.Optional;

public class EnvUtils {

    public static Optional<String> getEnv(String envName) {
        return Optional.ofNullable(System.getenv(envName)).map(s -> {
            if (s == null || s.isBlank()) {
                return null;
            }
            return s;
        });
    }

    public static String getEnvOrDefault(String envName, String defaultValue) {
        return getEnv(envName).orElse(defaultValue);
    }

}
