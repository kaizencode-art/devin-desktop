package io.github.kaizensphere.devin.devindesktop.models;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

public record EnvVariableModel(UUID id, String name, String value, Instant updatedDate) {
    

    public EnvVariableModel(String name, String value) {
        this(UUID.randomUUID(), name, value, Instant.now());
    }


    public EnvVariableModel(UUID id, String name, String value) {
        this(id, name, value, Instant.now());
    }

    public EnvVariableModel(UUID id, String name, String value, Instant updatedDate) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.updatedDate = this.setUpdateDate();
    }

    private Instant setUpdateDate() {
        if((name != null && !name.isEmpty()) && (value != null && !value.isEmpty())) return Instant.now();
        return null;
    }
}
