package dev.kaizensphere.devin.p2p.internal.recording;

import dev.kaizensphere.devin.p2p.P2PTransport;
import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;

import java.util.function.Consumer;

public class RecordingTransport implements P2PTransport {
    private Message sentMessage;
    private Consumer<Message> receiver;

    @Override
    public void register(Consumer<Message> receiver) {
        this.receiver = receiver;
    }

    @Override
    public void send(Message message) {
        this.sentMessage = message;
    }

    public Message sentMessage() {
        return sentMessage;
    }

    public void simulateIncoming(Message message) {
        receiver.accept(message);
    }

    public void simulateIncoming(NodeId sender, byte[] payload) {
        receiver.accept(new Message(sender, payload));
    }
}
