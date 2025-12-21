package dev.kaizensphere.devin.model;

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
}
