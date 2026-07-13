package dev.kaizensphere.devin.p2p.entity;

import java.util.Objects;

public record NodeId(String value) {
    public NodeId {
        Objects.requireNonNull(value);

        if(value.isBlank()) {
            throw new IllegalArgumentException("Node ID cannot be blank");
        }
    }
}
