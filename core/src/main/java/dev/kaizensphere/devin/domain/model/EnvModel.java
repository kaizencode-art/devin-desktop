package dev.kaizensphere.devin.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record EnvModel(UUID id, String title, String description, Status status, List<EnvVariableModel> variables) {

    public EnvModel(UUID id, String title, String description, Status status, List<EnvVariableModel> variables) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.variables = variables;
    }

    public EnvModel(String title, String description, Status status, List<EnvVariableModel> variables) {
        this(UUID.randomUUID(), title, description, status, variables);
    }

    public EnvModel(UUID id, String title, String description, Status status) {
        this(id, title, description, status, List.of());
    }

    public EnvModel withNewVariable(EnvVariableModel newEnvVar) {
        List<EnvVariableModel> envVars = new ArrayList<>(this.variables());
        envVars.add(newEnvVar);
        return new EnvModel(title, description, status, envVars);
    }

    public EnvModel withUpdatedVariable(UUID oldEnvVariableId, EnvVariableModel updatedEnvVar) {
        List<EnvVariableModel> envVars = new ArrayList<>(this.variables());
        int updatedEnvVarIndex = findVarIndexById(envVars, oldEnvVariableId);
        if (updatedEnvVarIndex < 0) {
            return this.withNewVariable(updatedEnvVar);
        }
        envVars.set(updatedEnvVarIndex, updatedEnvVar);
        return new EnvModel(title, description, status, envVars);
    }

    public EnvModel withRemovedVariable(UUID envVariableId) {
        List<EnvVariableModel> envVars = new ArrayList<>(this.variables());
        int removedEnvVarIndex = findVarIndexById(envVars, envVariableId);
        if (removedEnvVarIndex < 0) return this;
        envVars.remove(removedEnvVarIndex);
        return new EnvModel(title, description, status, envVars);
    }

    public EnvModel(String title, String description, Status status) {
        this(title, description, status, List.of());
    }
    
    public enum Status {
        VALID("valid"),
        INVALID("invalid"),
        WARNING("warning");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    };

    private int findVarIndexById(List<EnvVariableModel> vars, UUID id) {
        for (int i = 0; i < vars.size(); i++) {
            if (vars.get(i).id().equals(id)) return i;
        }
        return -1;
    }
}
