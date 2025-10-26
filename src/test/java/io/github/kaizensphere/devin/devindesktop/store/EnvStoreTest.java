package io.github.kaizensphere.devin.devindesktop.store;


import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class EnvStoreTest {

    private EnvStore envStore;
    private EnvStoreListener mockListener;

    @BeforeEach
    void setUp() {
        envStore = new EnvStore();
        mockListener = Mockito.mock(EnvStoreListener.class);
        envStore.registerListener(mockListener);
    }

    @Test
    void shouldAddNewEnvSuccessfully() {
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
        verify(mockListener, times(1)).onEnvironmentChanged();
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    void shouldUpdateExistingEnvSuccessfully() {
        // Given
        EnvModel original = new EnvModel("Original", "Original Env", EnvModel.Status.WARNING);
        envStore.addEnv(original);
        clearInvocations(mockListener);

        // When
        EnvModel updated = new EnvModel(original.id(), "Updated", "Updated Env", EnvModel.Status.VALID);
        envStore.updateEnv(updated);

        // Then
        Optional<EnvModel> retrieved = envStore.getEnvById(original.id());
        assertTrue(retrieved.isPresent(), "Updated env should still be retrievable");
        assertEquals("Updated", retrieved.get().title(), "Env name should match the updated value");
        verify(mockListener, times(1)).onEnvironmentChanged();
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    void shouldGetAllEnvsOnHappyPath() {
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
    void shouldRemoveEnvSuccessfully() {
        // Given
        EnvModel env = new EnvModel(UUID.randomUUID(), "Remove Me", "this should delete the env", EnvModel.Status.VALID);
        envStore.addEnv(env);
        clearInvocations(mockListener);

        // When
        boolean removed = envStore.removeEnv(env);

        // Then
        assertTrue(removed, "Env should be successfully removed on happy path");
        assertFalse(envStore.getEnvs().contains(env), "Removed env should not be in the store anymore");
        assertTrue(envStore.getEnvById(env.id()).isEmpty(), "Removed env should not be retrievable");
        verify(mockListener, times(1)).onEnvironmentChanged();
        verifyNoMoreInteractions(mockListener);
    }
}
