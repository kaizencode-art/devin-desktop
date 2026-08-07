package dev.kaizensphere.devin.desktop.p2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.store.EnvStore;
import dev.kaizensphere.devin.p2p.DevinP2P;
import dev.kaizensphere.devin.p2p.transport.recording.RecordingTransport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EnvP2PSubscriberTest {
    
//    @Test
//    void should_send_all_environments_when_the_store_changes() {
//        var envs = List.of(new EnvModel("test-env", "Test environment", EnvModel.Status.VALID));
//        var store = new EnvStore(envs);
//        var transport = new RecordingTransport();
//        var codec = new EnvModelCodec(new ObjectMapper());
//
//        var p2p = new DevinP2P(transport, message -> {});
//        var subscriber = new EnvP2PSubscriber(store, p2p, codec);
////        subscriber.onEnvironmentChanged();
//
//        var decoded = codec.decode(transport.sentMessage().payload());
//
//        Assertions.assertEquals(envs, decoded);
//    }
//
//    @Test
//    void should_apply_received_environments_to_the_store() {
//        var envs = List.of(new EnvModel("test-env", "Test environment", EnvModel.Status.VALID));
//        var store = new EnvStore(envs);
//        var transport = new RecordingTransport();
//        var codec = new EnvModelCodec(new ObjectMapper());
//
//        var p2p = new DevinP2P(transport, message -> {});
//        var subscriber = new EnvP2PSubscriber(store, p2p, codec);
////        subscriber.onEnvironmentChanged();
//
//        var decoded = codec.decode(transport.sentMessage().payload());
//
//    }
}
