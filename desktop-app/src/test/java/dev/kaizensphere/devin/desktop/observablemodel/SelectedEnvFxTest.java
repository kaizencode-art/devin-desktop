package dev.kaizensphere.devin.desktop.observablemodel;

import dev.kaizensphere.devin.models.EnvModel;
import dev.kaizensphere.devin.models.EnvVariableModel;
import dev.kaizensphere.devin.store.EnvStore;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectedEnvFxTest {

    private EnvStore store;
    private SelectedEnvFx selectedEnvFx;

    @BeforeAll
    static void initFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already started
        }
    }

    @BeforeEach
    void setUp() {
        store = new EnvStore();
        selectedEnvFx = SelectedEnvFx.init(store);
    }

    @Test
    void should_clamp_index_correctly() {
        store.addEnv(new EnvModel("Env 1", "Desc", EnvModel.Status.VALID));
        store.addEnv(new EnvModel("Env 2", "Desc", EnvModel.Status.VALID));

        selectedEnvFx.setSelectedEnvIndex(-1);
        assertEquals(0, selectedEnvFx.getSelectedIndex());

        selectedEnvFx.setSelectedEnvIndex(5);
        assertEquals(1, selectedEnvFx.getSelectedIndex());
    }

    @Test
    void should_sync_when_env_removed() {
        EnvModel env1 = new EnvModel("Env 1", "Desc", EnvModel.Status.VALID);
        EnvModel env2 = new EnvModel("Env 2", "Desc", EnvModel.Status.VALID);
        store.addEnv(env1);
        store.addEnv(env2);

        selectedEnvFx.setSelectedEnvIndex(1); // Select env2
        assertEquals(1, selectedEnvFx.getSelectedIndex());
        assertEquals(env2, selectedEnvFx.getSelectedEnv());

        store.removeEnv(env2);

        assertEquals(0, selectedEnvFx.getSelectedIndex());
        assertEquals(env1, selectedEnvFx.getSelectedEnv());
    }

    @Test
    void should_handle_empty_store() {
        assertEquals(-1, selectedEnvFx.getSelectedIndex());
        assertNull(selectedEnvFx.getSelectedEnv());
        
        store.addEnv(new EnvModel("Env 1", "Desc", EnvModel.Status.VALID));
        selectedEnvFx.setSelectedEnvIndex(0);
        
        store.removeEnv(store.getEnvs().get(0));
        assertEquals(-1, selectedEnvFx.getSelectedIndex());
        assertNull(selectedEnvFx.getSelectedEnv());
    }

    @Test
    void should_maintain_selection_by_index_when_id_changes() {
        // Given
        EnvModel env1 = new EnvModel("Env 1", "Desc", EnvModel.Status.VALID);
        store.addEnv(env1);
        selectedEnvFx.setSelectedEnvIndex(0);
        UUID oldId = env1.id();
        assertEquals(oldId, selectedEnvFx.getSelectedEnv().id());

        // When
        store.addEnvVariable(oldId, new EnvVariableModel("VAR", "VAL"));

        // Then
        assertEquals(0, selectedEnvFx.getSelectedIndex());
        assertNotEquals(oldId, selectedEnvFx.getSelectedEnv().id(), "ID should have changed");
        assertEquals("Env 1", selectedEnvFx.getSelectedEnv().title());
    }
}
