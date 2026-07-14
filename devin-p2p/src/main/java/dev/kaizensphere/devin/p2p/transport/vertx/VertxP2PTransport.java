package dev.kaizensphere.devin.p2p.transport.vertx;

import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;
import dev.kaizensphere.devin.p2p.transport.P2PTransport;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

public class VertxP2PTransport implements P2PTransport {

    private static final String ADDRESS = "devin.p2p.messages";
    private final EventBus eventBus;
    private MessageConsumer<Buffer> consumer;

    public VertxP2PTransport(Vertx vertx) {
        this.eventBus = vertx.eventBus();
    }

    @Override
    public Future<Void> start(Consumer<Message> handler) {
        if(consumer != null) {
            return Future.failedFuture("Transport already started");
        }
        this.consumer = eventBus.<Buffer>consumer(ADDRESS);

        consumer.handler( event -> {
            var message = decode(event.body());
            handler.accept(message);
        });
        return consumer.completion();
    }

    @Override
    public void send(Message message) {
        eventBus.publish(ADDRESS, encode(message));
    }

    @Override
    public Future<Void> close() {
        if(consumer == null) {
            return Future.succeededFuture();
        }
        var consumerToClose = consumer;
        consumer = null;
        return consumerToClose.unregister();
    }

    private Buffer encode(Message message) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(message.sender());
        Objects.requireNonNull(message.payload());

        var senderBytes = message.sender().value().getBytes(StandardCharsets.UTF_8);

        if(senderBytes.length > Integer.MAX_VALUE - Integer.BYTES) {
            throw new IllegalArgumentException("Sender id too long");
        }

        return Buffer.buffer(Integer.BYTES + senderBytes.length + message.payload().length)
                .appendInt(senderBytes.length)
                .appendBytes(senderBytes)
                .appendBytes(message.payload());
    }

    private Message decode(Buffer buffer) {
        if(buffer.length() < Integer.BYTES) {
            throw new IllegalArgumentException("Invalid message");
        }

        var senderLength = buffer.getInt(0);
        var senderStart = Integer.BYTES;
        var senderEnd = senderStart + senderLength;

        if(senderLength < 0 || senderEnd > buffer.length()) {
            throw new IllegalArgumentException("Invalid sender length");
        }

        var sender = buffer.getString(
                senderStart,
                senderEnd,
                StandardCharsets.UTF_8.name()
        );

        var payload = buffer.getBytes(senderEnd, buffer.length());

        return new Message(new NodeId(sender), payload);
    }
}
