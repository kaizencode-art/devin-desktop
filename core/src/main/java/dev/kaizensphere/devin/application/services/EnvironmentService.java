package dev.kaizensphere.devin.application.services;

import dev.kaizensphere.devin.application.ports.in.EnvironmentUseCases;
import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.model.EnvVariableModel;
import dev.kaizensphere.devin.domain.store.EnvStore;

import java.util.ArrayList;
import java.util.UUID;

public class EnvironmentService implements EnvironmentUseCases {
    private final EnvStore envStore;

    public EnvironmentService(EnvStore envStore) {
        this.envStore = envStore;
    }

    @Override
    public void createNewEnvironment() {
        EnvModel newEnv = new EnvModel("New Environment", "", EnvModel.Status.VALID, new ArrayList<>());
        envStore.addEnv(newEnv);
    }

    @Override
    public void createNewEnvironmentVariable(UUID envId) {
        if (envId == null) return;
        envStore.getEnvById(envId).ifPresent(env -> {
            EnvVariableModel newVar = new EnvVariableModel("NEW_VAR", "value");
            EnvModel updated = env.withNewVariable(newVar);
            envStore.updateEnv(envId, updated);
        });
    }

    @Override
    public void importEnvironment() {
        // Logique d'importation à implémenter (ex: via un FilePort)
        System.out.println("[DEBUG_LOG] importEnvironment called");
    }

    @Override
    public void exportEnvironment(UUID envId) {
        // Logique d'exportation à implémenter
        System.out.println("[DEBUG_LOG] exportEnvironment called for " + envId);
    }

    @Override
    public void deleteEnvironment(UUID envId) {
        if (envId == null) return;
        envStore.getEnvById(envId).ifPresent(envStore::removeEnv);
    }

    @Override
    public void updateEnvironment(UUID id, String title, String description) {
        if (id == null) return;
        envStore.getEnvById(id).ifPresent(env -> {
            EnvModel updated = new EnvModel(title, description, env.status(), env.variables());
            envStore.updateEnv(id, updated);
        });
    }

    @Override
    public void updateEnvVariable(UUID envId, UUID varId, String name, String value) {
        if (envId == null || varId == null) return;
        envStore.getEnvById(envId).ifPresent(env -> {
            EnvVariableModel updatedVar = new EnvVariableModel(varId, name, value);
            EnvModel updated = env.withUpdatedVariable(varId, updatedVar);
            envStore.updateEnv(envId, updated);
        });
    }

    @Override
    public void deleteEnvVariable(UUID envId, UUID varId) {
        if (envId == null || varId == null) return;
        envStore.getEnvById(envId).ifPresent(env -> {
            EnvModel updated = env.withRemovedVariable(varId);
            envStore.updateEnv(envId, updated);
        });
    }

    @Override
    public void openRecentEnvironment() {
        System.out.println("[DEBUG_LOG] openRecentEnvironment called");
    }
}
