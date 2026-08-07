package dev.kaizensphere.devin.store;

import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.store.EnvStore;
import dev.kaizensphere.devin.domain.store.EnvStoreListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnvStoreListenerTest {

    @Test
    void should_notify_when_update_happen_on_store() {
        var env = new EnvModel("intégration", "developper environment", EnvModel.Status.VALID);
        EnvStore store = new EnvStore();

        store.registerListener(new EnvStoreListener() {
            @Override
            public void onEnvironmentChanged(Change change) {
                assertTrue(true);
            }
        });
        store.addEnv(env);
    }

    @Test
    void should_notify_env_added_to_store() {
        var env = new EnvModel("intégration", "developper environment", EnvModel.Status.VALID);
        EnvStore store = new EnvStore();

        store.registerListener(new EnvStoreListener() {
            @Override
            public void onEnvironmentChanged(Change change) {
                if(change instanceof Change.EnvAdded(EnvModel env1)) {
                    assertEquals("intégration", env1.title());
                }
            }
        });
        store.addEnv(env);
    }

    @Test
    void should_notify_listeners_with_old_and_new_env_when_updated() {
        var previousEnv = new EnvModel("intégration", "old description", EnvModel.Status.VALID);
        var currentEnv = new EnvModel("intégration", "new description", EnvModel.Status.VALID);
        EnvStore store = new EnvStore(new ArrayList<>( List.of(currentEnv)));
        store.registerListener(new EnvStoreListener() {
            @Override
            public void onEnvironmentChanged(Change change) {
                if(change instanceof Change.EnvUpdated(EnvModel previous, EnvModel current)) {
                    assertEquals("old description", previous.title());
                    assertEquals("new description", current.title());
                }
            }
        });
        store.updateEnv(previousEnv.id(), currentEnv);
        var update = store.getEnvs().stream()
                .filter(env -> env.title().equals("intégration"))
                .findFirst()
                .orElseThrow();
        assertEquals("new description", update.description());
    }

    @Test
    void should_notify_listeners_with_the_env_name_removed() {
        var env1 = new EnvModel("intégration", "developper environment", EnvModel.Status.VALID);
        var env2 = new EnvModel("production", "production environment", EnvModel.Status.VALID);
        var envs = new ArrayList<>(List.of(env1, env2));
        EnvStore store = new EnvStore(envs);
        final boolean[] deleted = {false};
        store.registerListener(new EnvStoreListener() {
            @Override
            public void onEnvironmentChanged(Change change) {
                if (change instanceof Change.EnvDeleted(EnvModel env)) {
                    deleted[0] = true;
                    assertEquals("production", env.title());
                }
            }
        });
        store.removeEnv(env2);
        assertTrue( deleted[0]);
        assertFalse(envs.contains(env2));
    }

    @Test
    void should_notify_when_update_happen_with_the_name_of_the_changed_env() {
        var env = new EnvModel("intégration", "developper environment", EnvModel.Status.VALID);
        EnvStore store = new EnvStore();

        store.registerListener(new EnvStoreListener() {
            @Override
            public void onEnvironmentChanged(Change change) {
                if (change instanceof Change.EnvAdded(EnvModel env1)) {
                    assertEquals("intégration", env1.title());
                }
            }
        });

        store.addEnv(env);
    }
}
