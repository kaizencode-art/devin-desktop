package dev.kaizensphere.devin.p2p.internal.recording;

import dev.kaizensphere.devin.p2p.P2PMessageHandler;
import dev.kaizensphere.devin.p2p.entity.Message;

public class RecordingMessageHandler implements P2PMessageHandler {

    private Message receivedPayload;

    @Override
    public void handle(Message payload) {
        this.receivedPayload = payload;
    }

    public Message receivedMessage() {
        return receivedPayload;
    }
}
