package dev.kaizensphere.devin.p2p.transport.vertx;

import com.hazelcast.config.XmlConfigBuilder;
import dev.kaizensphere.devin.p2p.DevinP2P;
import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class VertxP2PTransportTest {

    private CompletableFuture<Vertx> createClusteredVertx() throws FileNotFoundException {
        var configStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("hazel-cluster.xml");

        if (configStream == null) {
            throw new FileNotFoundException("hazel-cluster.xml not found in test resources");
        }

        var config = new XmlConfigBuilder(configStream).build();
        var clusterManager = new HazelcastClusterManager(config);

        var eventBusOptions = new EventBusOptions()
                .setHost("127.0.0.1")
                .setClusterPublicHost("127.0.0.1");

        var vertxOptions = new VertxOptions()
                .setEventBusOptions(eventBusOptions);

        return Vertx.builder()
                .with(vertxOptions)
                .withClusterManager(clusterManager)
                .buildClustered()
                .toCompletionStage()
                .toCompletableFuture();
    }

    @Test
    public void should_send_a_message_after_receiver_registration_completes() throws Exception{
        var vertx1 = createClusteredVertx().get();
        var vertx2 = createClusteredVertx().get();

        try {
            var sender = new VertxP2PTransport(vertx1);
            var receiver = new VertxP2PTransport(vertx2);

            var received = new CompletableFuture<Message>();
            receiver.start(received::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            var expectedMessage = new Message(
                    new NodeId("test"),
                    new byte[]{1, 2, 3}
            );

            sender.send(expectedMessage);
            var actual = received.get(10, TimeUnit.SECONDS);
            assertEquals(expectedMessage.sender(), actual.sender());
            assertArrayEquals(expectedMessage.payload(), actual.payload());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
            vertx2.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_send_a_message_between_two_devin_p2p_vertx_transports() throws Exception{
        var vertx1 = createClusteredVertx().get();
        var vertx2 = createClusteredVertx().get();

        try {
            var received = new CompletableFuture<Message>();
            var node1 = new DevinP2P(
                    new NodeId("node-1"),
                    new VertxP2PTransport(vertx1),
                    message -> {}
            );
            var node2 = new DevinP2P(
                    new NodeId("node-2"),
                    new VertxP2PTransport(vertx2),
                    received::complete
            );
            node1.ready().toCompletionStage().toCompletableFuture().get();
            node2.ready().toCompletionStage().toCompletableFuture().get();

            var payload = new byte[]{1, 2, 3};

            node1.send(payload);

            var actual = received.get(10, TimeUnit.SECONDS);

            assertEquals(new NodeId("node-1"), actual.sender());
            assertArrayEquals(payload, actual.payload());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
            vertx2.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_broadcast_a_message_to_all_others() throws Exception {
        var vertx1 = createClusteredVertx().get();
        var vertx2 = createClusteredVertx().get();
        var vertx3 = createClusteredVertx().get();

        try {
            var received2 = new CompletableFuture<Message>();
            var received3 = new CompletableFuture<Message>();

            var node1 = new DevinP2P(
                    new NodeId("node-1"),
                    new VertxP2PTransport(vertx1),
                    message -> {
                    }
            );
            var node2 = new DevinP2P(
                    new NodeId("node-2"),
                    new VertxP2PTransport(vertx2),
                    received2::complete
            );
            var node3 = new DevinP2P(
                    new NodeId("node-3"),
                    new VertxP2PTransport(vertx3),
                    received3::complete
            );

            node1.ready().toCompletionStage().toCompletableFuture().get();
            node2.ready().toCompletionStage().toCompletableFuture().get();
            node3.ready().toCompletionStage().toCompletableFuture().get();

            var payload = new byte[]{1, 2, 3};

            node1.send(payload);

            var actual2 = received2.get(10, TimeUnit.SECONDS);
            var actual3 = received3.get(10, TimeUnit.SECONDS);

            assertEquals(new NodeId("node-1"), actual2.sender());
            assertArrayEquals(payload, actual2.payload());

            assertEquals(new NodeId("node-1"), actual3.sender());
            assertArrayEquals(payload, actual3.payload());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
            vertx2.close().toCompletionStage().toCompletableFuture().get();
            vertx3.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_stop_receiving_messages_after_close() throws Exception {
        var vertx1 = createClusteredVertx().get();
        var vertx2 = createClusteredVertx().get();

        try {
            var sender = new VertxP2PTransport(vertx1);
            var receiver = new VertxP2PTransport(vertx2);

            var received = new CompletableFuture<Message>();
            receiver.start(received::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            receiver.close();

            var message = new Message(
                    new NodeId("test"),
                    new byte[]{1, 2, 3}
            );

            sender.send(message);

            assertThrows(java.util.concurrent.TimeoutException.class, () -> {
                received.get(2, TimeUnit.SECONDS);
            });
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
            vertx2.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_fail_when_transport_is_started_twice() throws Exception {
        var vertx1 = createClusteredVertx().get();

        try {
            var transport = new VertxP2PTransport(vertx1);

            transport.start(message -> {
                    })
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            var secondStart = transport.start(message -> {
                    })
                    .toCompletionStage()
                    .toCompletableFuture();

            var exception = assertThrows(java.util.concurrent.ExecutionException.class, () -> {
                secondStart.get(10, TimeUnit.SECONDS);
            });

            assertNotNull(exception.getCause());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_allow_starting_again_after_close() throws Exception {
        var vertx1 = createClusteredVertx().get();
        var vertx2 = createClusteredVertx().get();

        try {
            var sender = new VertxP2PTransport(vertx1);
            var receiver = new VertxP2PTransport(vertx2);

            var received1 = new CompletableFuture<Message>();
            receiver.start(received1::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            receiver.close();

            var received2 = new CompletableFuture<Message>();
            receiver.start(received2::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            var expectedMessage = new Message(
                    new NodeId("test"),
                    new byte[]{1, 2, 3}
            );

            sender.send(expectedMessage);
            var actual = received2.get(10, TimeUnit.SECONDS);
            assertEquals(expectedMessage.sender(), actual.sender());
            assertArrayEquals(expectedMessage.payload(), actual.payload());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
            vertx2.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_not_deliver_messages_to_the_previous_handler_after_restart() throws Exception {
        var vertx1 = createClusteredVertx().get();
        var vertx2 = createClusteredVertx().get();

        try {
            var sender = new VertxP2PTransport(vertx1);
            var receiver = new VertxP2PTransport(vertx2);

            var received1 = new CompletableFuture<Message>();
            receiver.start(received1::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            receiver.close()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            var received2 = new CompletableFuture<Message>();
            receiver.start(received2::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            var message = new Message(
                    new NodeId("test"),
                    new byte[]{1, 2, 3}
            );

            sender.send(message);

            assertThrows(java.util.concurrent.TimeoutException.class, () -> {
                received1.get(2, TimeUnit.SECONDS);
            });

            var actual = received2.get(10, TimeUnit.SECONDS);
            assertEquals(message.sender(), actual.sender());
            assertArrayEquals(message.payload(), actual.payload());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get();
            vertx2.close().toCompletionStage().toCompletableFuture().get();
        }
    }

    @Test
    public void should_resume_message_delivery_after_receiver_reconnects()
            throws Exception {

        var vertx1 = createClusteredVertx()
                .get(10, TimeUnit.SECONDS);

        var vertx2 = createClusteredVertx()
                .get(10, TimeUnit.SECONDS);

        Vertx vertx3 = null;

        try {
            var sender = new VertxP2PTransport(vertx1);

            var receiver = new VertxP2PTransport(vertx2);

            receiver.start(message -> {})
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            // Le deuxième nœud quitte complètement le cluster.
            vertx2.close()
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            // Un nouveau runtime rejoint le même cluster.
            vertx3 = createClusteredVertx()
                    .get(10, TimeUnit.SECONDS);

            var reconnectedReceiver = new VertxP2PTransport(vertx3);
            var receivedAfterReconnect = new CompletableFuture<Message>();

            reconnectedReceiver.start(receivedAfterReconnect::complete)
                    .toCompletionStage()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);

            var expectedMessage = new Message(
                    new NodeId("test"),
                    new byte[]{1, 2, 3}
            );

            sender.send(expectedMessage);

            var actual = receivedAfterReconnect
                    .get(10, TimeUnit.SECONDS);

            assertEquals(expectedMessage.sender(), actual.sender());
            assertArrayEquals(expectedMessage.payload(), actual.payload());

        } finally {
            try {
                vertx1.close()
                        .toCompletionStage()
                        .toCompletableFuture()
                        .get(10, TimeUnit.SECONDS);
            } finally {
                if (vertx3 != null) {
                    vertx3.close()
                            .toCompletionStage()
                            .toCompletableFuture()
                            .get(10, TimeUnit.SECONDS);
                }
            }
        }
    }

}