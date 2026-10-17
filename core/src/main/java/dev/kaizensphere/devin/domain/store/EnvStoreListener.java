package dev.kaizensphere.devin.domain.store;

import dev.kaizensphere.devin.domain.model.EnvModel;

public interface EnvStoreListener {
    void onEnvironmentChanged(Change change);

    sealed interface Change
            permits Change.EnvAdded,
            Change.EnvUpdated,
            Change.EnvDeleted,
            Change.EnvRemoved {
        record EnvAdded(EnvModel env) implements Change {}
        record EnvUpdated(EnvModel oldEnv, EnvModel newEnv) implements Change {}
        record EnvDeleted(EnvModel env) implements Change {}
        record EnvRemoved(String envName) implements Change {}
    }
}
