package dev.kaizensphere.devin.store;

import dev.kaizensphere.devin.models.EnvModel;
import dev.kaizensphere.devin.models.EnvVariableModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class EnvStoreTest {

    private EnvStore envStore;
    private EnvStoreListener mockListener;

    private int notificationsCount = 0;

    @BeforeEach
    void setUp() {
        envStore = new EnvStore();
        notificationsCount = 0;
        mockListener = () -> notificationsCount++;
        envStore.registerListener(mockListener);
    }

    @Test
    void should_add_new_env_successfully() {
        // Given
        EnvModel env = new EnvModel(UUID.randomUUID(), "Test Env", "Test Env Desc", EnvModel.Status.VALID);

        // When
        envStore.addEnv(env);

        // Then
        List<EnvModel> allEnvs = envStore.getEnvs();
        assertTrue(allEnvs.contains(env), "The newly added env should be in the store");
        Optional<EnvModel> retrieved = envStore.getEnvById(env.id());
        assertTrue(retrieved.isPresent(), "Env should be retrievable by its ID");
        assertEquals(env, retrieved.get(), "Retrieved env should match the added env");
        assertEquals(1, notificationsCount);
    }

    @Test
    void should_update_existing_env_successfully() {
        // Given
        EnvModel original = new EnvModel("Original", "Original Env", EnvModel.Status.WARNING);
        envStore.addEnv(original);
        notificationsCount = 0;

        // When
        EnvModel updated = new EnvModel(original.id(), "Updated", "Updated Env", EnvModel.Status.VALID);
        envStore.updateEnv(updated);

        // Then
        Optional<EnvModel> retrieved = envStore.getEnvById(original.id());
        assertTrue(retrieved.isPresent(), "Updated env should still be retrievable");
        assertEquals("Updated", retrieved.get().title(), "Env name should match the updated value");
        assertEquals(1, notificationsCount);
    }

    @Test
    void should_get_all_envs_on_happy_path() {
        // Given
        EnvModel env1 = new EnvModel(UUID.randomUUID(), "Env One", "The first environment", EnvModel.Status.VALID);
        EnvModel env2 = new EnvModel(UUID.randomUUID(), "Env Two", "The second environment", EnvModel.Status.WARNING);
        envStore.addEnv(env1);
        envStore.addEnv(env2);

        // When
        List<EnvModel> allEnvs = envStore.getEnvs();

        // Then
        assertEquals(2, allEnvs.size(), "Store should have two envs");
        assertTrue(allEnvs.contains(env1) && allEnvs.contains(env2),
                "All added envs should appear in getEnvs()");
    }

    @Test
    void should_remove_env_successfully() {
        // Given
        EnvModel env = new EnvModel(UUID.randomUUID(), "Remove Me", "this should delete the env", EnvModel.Status.VALID);
        envStore.addEnv(env);
        notificationsCount = 0;

        // When
        boolean removed = envStore.removeEnv(env);

        // Then
        assertTrue(removed, "Env should be successfully removed on happy path");
        assertFalse(envStore.getEnvs().contains(env), "Removed env should not be in the store anymore");
        assertTrue(envStore.getEnvById(env.id()).isEmpty(), "Removed env should not be retrievable");
        assertEquals(1, notificationsCount);
    }

    @Test
    void should_add_envVariable_successfully() {
        // Given
        EnvModel env = new EnvModel("Test Env", "Desc", EnvModel.Status.VALID);
        envStore.addEnv(env);
        UUID oldId = env.id();
        EnvVariableModel var = new EnvVariableModel("VAR1", "VAL1");
        notificationsCount = 0;

        // When
        envStore.addEnvVariable(oldId, var);

        // Then
        List<EnvModel> envs = envStore.getEnvs();
        assertEquals(1, envs.size());
        EnvModel updated = envs.get(0);
        assertNotEquals(oldId, updated.id(), "Env ID should change after variable addition");
        assertEquals(1, updated.variables().size());
        assertEquals("VAR1", updated.variables().get(0).name());
        assertEquals(1, notificationsCount);
    }

    @Test
    void should_update_envVariable_successfully() {
        // Given
        EnvVariableModel var = new EnvVariableModel("VAR1", "VAL1");
        EnvModel env = new EnvModel("Test Env", "Desc", EnvModel.Status.VALID, List.of(var));
        envStore.addEnv(env);
        UUID oldEnvId = env.id();
        EnvVariableModel updatedVar = new EnvVariableModel(var.id(), "VAR1_UPDATED", "VAL1_UPDATED");
        notificationsCount = 0;

        // When
        envStore.updateEnvVariable(oldEnvId, var.id(), updatedVar);

        // Then
        List<EnvModel> envs = envStore.getEnvs();
        assertEquals(1, envs.size());
        EnvModel updatedEnv = envs.get(0);
        assertNotEquals(oldEnvId, updatedEnv.id(), "Env ID should change after variable update");
        assertEquals("VAR1_UPDATED", updatedEnv.variables().get(0).name());
        assertEquals("VAL1_UPDATED", updatedEnv.variables().get(0).value());
        assertEquals(1, notificationsCount);
    }

    @Test
    void should_remove_envVariable_successfully() {
        // Given
        EnvVariableModel var = new EnvVariableModel("VAR1", "VAL1");
        EnvModel env = new EnvModel("Test Env", "Desc", EnvModel.Status.VALID, List.of(var));
        envStore.addEnv(env);
        UUID oldEnvId = env.id();
        notificationsCount = 0;

        // When
        envStore.removeEnvVariable(oldEnvId, var.id());

        // Then
        List<EnvModel> envs = envStore.getEnvs();
        assertEquals(1, envs.size());
        EnvModel updatedEnv = envs.get(0);
        assertNotEquals(oldEnvId, updatedEnv.id(), "Env ID should change after variable removal");
        assertTrue(updatedEnv.variables().isEmpty());
        assertEquals(1, notificationsCount);
    }
}
