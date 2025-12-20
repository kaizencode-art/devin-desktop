package dev.kaizensphere.devin.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvValidationUtilsTest {

    @Test
    void should_sanitize_variable_name() {
        assertEquals("VAR1", dev.kaizensphere.devin.utils.EnvValidationUtils.sanitizeVariableName("VAR-1!"));
        assertEquals("abc123ABC", dev.kaizensphere.devin.utils.EnvValidationUtils.sanitizeVariableName("abc-123_ABC"));
        assertEquals("", dev.kaizensphere.devin.utils.EnvValidationUtils.sanitizeVariableName("!!!"));
        assertEquals("", dev.kaizensphere.devin.utils.EnvValidationUtils.sanitizeVariableName(null));
    }

    @Test
    void should_format_variable_name() {
        assertEquals("VAR1", dev.kaizensphere.devin.utils.EnvValidationUtils.formatVariableName("var1"));
        assertEquals("VAR1", dev.kaizensphere.devin.utils.EnvValidationUtils.formatVariableName("VAR1"));
        assertEquals("", dev.kaizensphere.devin.utils.EnvValidationUtils.formatVariableName(null));
    }
}
