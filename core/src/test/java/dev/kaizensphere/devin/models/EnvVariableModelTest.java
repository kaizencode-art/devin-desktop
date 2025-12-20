package dev.kaizensphere.devin.models;

import dev.kaizensphere.devin.models.EnvVariableModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EnvVariableModelTest {

    @Test
    void should_set_updated_date_when_name_and_value_present() {
        EnvVariableModel var = new EnvVariableModel("NAME", "VALUE");
        assertNotNull(var.updatedDate());
    }

    @Test
    void should_set_updated_date_to_null_when_name_empty() {
        EnvVariableModel var = new EnvVariableModel("", "VALUE");
        assertNull(var.updatedDate());
    }

    @Test
    void should_set_updated_date_to_null_when_value_empty() {
        EnvVariableModel var = new EnvVariableModel("NAME", "");
        assertNull(var.updatedDate());
    }

    @Test
    void should_set_updated_date_to_null_when_both_empty() {
        EnvVariableModel var = new EnvVariableModel("", "");
        assertNull(var.updatedDate());
    }

    @Test
    void should_preserve_id() {
        UUID id = UUID.randomUUID();
        EnvVariableModel var = new EnvVariableModel(id, "NAME", "VALUE");
        assertEquals(id, var.id());
    }
}
