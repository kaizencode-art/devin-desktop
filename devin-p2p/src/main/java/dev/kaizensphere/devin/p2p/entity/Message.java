package dev.kaizensphere.devin.p2p.entity;

import java.util.Objects;

public record Message(NodeId sender, byte[] payload) {

    public Message {
        Objects.requireNonNull(sender);
        Objects.requireNonNull(payload);
        payload = payload.clone();
    }

    @Override
    public byte[] payload() {
        return payload.clone()  ;
    }
}
