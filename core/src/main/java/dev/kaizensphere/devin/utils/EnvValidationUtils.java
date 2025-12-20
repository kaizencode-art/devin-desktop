package dev.kaizensphere.devin.utils;

public class EnvValidationUtils {

    /**
     * Sanitizes a variable name by removing non-alphanumeric characters.
     * @param name The name to sanitize
     * @return The sanitized name
     */
    public static String sanitizeVariableName(String name) {
        if (name == null) return "";
        return name.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * Formats a variable name (e.g., to uppercase).
     * @param name The name to format
     * @return The formatted name
     */
    public static String formatVariableName(String name) {
        if (name == null) return "";
        return name.toUpperCase();
    }
}
