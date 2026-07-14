package dev.kaizensphere.devin.p2p.transport;

import dev.kaizensphere.devin.p2p.entity.Message;
import io.vertx.core.Future;

import java.util.function.Consumer;

public interface P2PTransport {

    Future<Void> start(Consumer<Message> handler);
    void send(Message message);
    Future<Void> close();
}
