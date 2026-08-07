package dev.kaizensphere.devin.desktop.p2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.config.XmlConfigBuilder;
import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.p2p.DevinP2P;
import dev.kaizensphere.devin.p2p.entity.Message;
import dev.kaizensphere.devin.p2p.entity.NodeId;
import dev.kaizensphere.devin.p2p.handler.P2PMessageHandler;
import dev.kaizensphere.devin.p2p.transport.vertx.VertxP2PTransport;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class VertxP2PEnvSyncTest {

    @Test
    void should_send_an_encoded_env_model_to_another_node() throws Exception {
        var vertx1 = createClusteredVertx().get(10, TimeUnit.SECONDS);
        var vertx2 = createClusteredVertx().get(10, TimeUnit.SECONDS);

        try {
            var envModels = List.of(new EnvModel("test-env", "Test environment", EnvModel.Status.VALID));
            var codec = new EnvModelCodec(new ObjectMapper());
            var received = new CompletableFuture<Message>();

            var node1 = new DevinP2P(new NodeId("node-1"), new VertxP2PTransport(vertx1), message -> {});
            var node2 = new DevinP2P(new NodeId("node-2"), new VertxP2PTransport(vertx2), received::complete);

            node1.ready().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
            node2.ready().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);

            byte[] encodedEnvModel = codec.encode(envModels);
            node1.send(encodedEnvModel);

            var actual = received.get(10, TimeUnit.SECONDS);
            assertEquals(new NodeId("node-1"), actual.sender());

            var decodedModel = codec.decode(actual.payload());
            assertEquals("test-env", decodedModel.getFirst().title());
            assertEquals("Test environment", decodedModel.getFirst().description());
            assertEquals(EnvModel.Status.VALID, decodedModel.getFirst().status());
        } finally {
            vertx1.close().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
            vertx2.close().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
        }
    }

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
}
