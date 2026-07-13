package dev.kaizensphere.devin.p2p;

import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;

import java.util.Objects;
import java.util.UUID;

public class DevinP2P {

    private final P2PTransport transport;
    private final P2PMessageHandler handler;
    private final NodeId nodeId;

    public DevinP2P(NodeId nodeId, P2PTransport transport, P2PMessageHandler handler) {
        this.nodeId = Objects.requireNonNull(nodeId);
        this.transport = Objects.requireNonNull(transport);
        this.handler = Objects.requireNonNull(handler);

        this.transport.register(this::receive);
    }

    public DevinP2P(P2PTransport transport, P2PMessageHandler handler) {
        this(new NodeId(UUID.randomUUID().toString()), transport, handler);
    }

    public NodeId nodeId() {
        return nodeId;
    }

    public void send(byte [] payload) {
        var message = new Message(nodeId, payload);
        transport.send(message);
    }

    private void receive(Message message) {
        if(this.nodeId.equals(message.sender())) {
            return;
        }
        handler.handle(message);
    }
}
