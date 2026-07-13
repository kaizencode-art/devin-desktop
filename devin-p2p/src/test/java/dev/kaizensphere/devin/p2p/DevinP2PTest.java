package dev.kaizensphere.devin.p2p;

import dev.kaizensphere.devin.p2p.entity.NodeId;
import dev.kaizensphere.devin.p2p.internal.recording.RecordingMessageHandler;
import dev.kaizensphere.devin.p2p.internal.recording.RecordingTransport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DevinP2PTest {

    @Test
    public void should_send_message_through_the_transport() {
        var transport = new RecordingTransport();
        var handler = new RecordingMessageHandler();
        var p2p = new DevinP2P(transport, handler);
        var payload = new byte[] {1, 2, 3};

        p2p.send(payload);

        Assertions.assertArrayEquals(payload, transport.sentMessage().payload());
    }

//    @Test
//    public void should_forward_a_received_payload_to_a_handler() {
//        var transport = new RecordingTransport();
//        var handler = new RecordingMessageHandler();
//        var p2p = new DevinP2P(transport, handler);
//        var payload = new byte[] {1, 2, 3};
//
//        p2p.receive(payload);
//        Assertions.assertArrayEquals(payload, handler.receivedPayload());
//    }

    @Test
    public void should_forward_a_received_payload_to_a_handler() {
        var transport = new RecordingTransport();
        var handler = new RecordingMessageHandler();
        var p2p = new DevinP2P(transport, handler);

        var payload = new byte[] {1, 2, 3};
        transport.simulateIncoming(new NodeId("node-42"), payload);

        Assertions.assertArrayEquals(payload, handler.receivedMessage().payload());
    }

    @Test
    public void should_ignore_messages_sent_by_itself() {
        var transport = new RecordingTransport();
        var handler = new RecordingMessageHandler();
        var nodeId = new NodeId("node-1");
        var p2p = new DevinP2P(nodeId, transport, handler);

        var payload = new byte[] {1, 2, 3};
        transport.simulateIncoming(p2p.nodeId(), payload);

        Assertions.assertNull(handler.receivedMessage());
    }

    @Test
    public void should_expose_its_node_id() {
        var nodeId = new NodeId("node-1");
        var transport = new RecordingTransport();
        var handler = new RecordingMessageHandler();
        var p2p = new DevinP2P(nodeId, transport, handler);

        Assertions.assertEquals(nodeId, p2p.nodeId());
    }

    @Test
    public void should_generate_distinct_node_ids() {
        var p2p1 = new DevinP2P(new RecordingTransport(), new RecordingMessageHandler());
        var p2p2 = new DevinP2P(new RecordingTransport(), new RecordingMessageHandler());

        Assertions.assertNotEquals(p2p1.nodeId(), p2p2.nodeId());
    }

    @Test
    public void should_attach_its_node_id_to_outgoing_messages() {
        var transport = new RecordingTransport();
        var handler = new RecordingMessageHandler();
        var p2p = new DevinP2P(new NodeId("node-42"), transport, handler);

        var payload = new byte[] {1, 2, 3};
        p2p.send(payload);
        Assertions.assertEquals(p2p.nodeId(), transport.sentMessage().sender());
    }

    @Test
    public void should_forward_a_received_message_to_a_handler() {
        var transport = new RecordingTransport();
        var handler = new RecordingMessageHandler();
        var p2p = new DevinP2P(new NodeId("node-1"), transport, handler);
        var senderNode = new NodeId("node-42");

        var payload = new byte[] {1, 2, 3};
        transport.simulateIncoming(senderNode, payload);
        Assertions.assertEquals(senderNode, handler.receivedMessage().sender());
        Assertions.assertArrayEquals(payload, handler.receivedMessage().payload());
    }
}
