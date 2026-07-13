package dev.kaizensphere.devin.p2p;

import dev.kaizensphere.devin.p2p.entity.Message;

public interface P2PMessageHandler {
    void handle(Message payload);
}
