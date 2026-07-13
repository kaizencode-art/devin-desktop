package dev.kaizensphere.devin.p2p;

import dev.kaizensphere.devin.p2p.entity.Message;

import java.util.function.Consumer;

public interface P2PTransport {

    void register(Consumer<Message> handler);
    void send(Message message);
}
