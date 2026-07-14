package dev.kaizensphere.devin.p2p.transport.recording;

import dev.kaizensphere.devin.p2p.transport.P2PTransport;
import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;
import io.vertx.core.Future;

import java.util.function.Consumer;

public class RecordingTransport implements P2PTransport {
    private Message sentMessage;
    private Consumer<Message> consumer;

    @Override
    public Future<Void> start(Consumer<Message> consumer) {
        this.consumer = consumer;
        return Future.succeededFuture();
    }

    @Override
    public void send(Message message) {
        this.sentMessage = message;
    }

    @Override
    public Future<Void> close() {
        consumer = null;
        return Future.succeededFuture();
    }

    public Message sentMessage() {
        return sentMessage;
    }

    public void simulateIncoming(Message message) {
        if (consumer != null) {
            consumer.accept(message);
        }
    }

    public void simulateIncoming(NodeId sender, byte[] payload) {
        simulateIncoming(new Message(sender, payload));
    }
}
