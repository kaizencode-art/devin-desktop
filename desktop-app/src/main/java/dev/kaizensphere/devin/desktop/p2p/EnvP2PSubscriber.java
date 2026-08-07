package dev.kaizensphere.devin.desktop.p2p;

import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.store.EnvStore;
import dev.kaizensphere.devin.domain.store.EnvStoreListener;
import dev.kaizensphere.devin.p2p.DevinP2P;
import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;
import dev.kaizensphere.devin.p2p.transport.P2PTransport;

public class EnvP2PSubscriber implements EnvStoreListener {

    private final EnvStore store;
    private final DevinP2P transport;
    private final EnvModelCodec codec;


    public EnvP2PSubscriber(EnvStore store, DevinP2P transport, EnvModelCodec codec) {
        this.store = store;
        this.transport = transport;
        this.codec = codec;
    }

    @Override
    public void onEnvironmentChanged(Change change) {
        switch (change) {
            case Change.EnvAdded(EnvModel env1):
                break;
            case Change.EnvDeleted(EnvModel env1):
                break;
            case Change.EnvUpdated(EnvModel oldEnv, EnvModel newEnv):
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + change);
        }
    }
}
