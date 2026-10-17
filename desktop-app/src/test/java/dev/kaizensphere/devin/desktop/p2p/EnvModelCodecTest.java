package dev.kaizensphere.devin.desktop.p2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kaizensphere.devin.domain.model.EnvModel;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnvModelCodecTest {

    @Test
    public void should_throw_exception_when_decoding_empty_payload() {
        // Given
        var codec = new EnvModelCodec(new ObjectMapper());
        byte[] emptyPayload = new byte[0];

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> codec.decode(emptyPayload));
    }

    @Test
    void should_reject_invalid_json() {
        var codec = new EnvModelCodec(new ObjectMapper());

        var invalidPayload = "not-json"
                .getBytes(StandardCharsets.UTF_8);

        assertThrows(
                IllegalArgumentException.class,
                () -> codec.decode(invalidPayload)
        );
    }

    @Test
    void should_reject_null_env_model() {
        var codec = new EnvModelCodec(new ObjectMapper());

        assertThrows(
                IllegalArgumentException.class,
                () -> codec.decode(null)
        );
    }

     @Test
    void should_encode_and_decode_a_list_of_env_model() {
        var codec = new EnvModelCodec(new ObjectMapper());
        var envModel1 = new EnvModel("test-env-1", "mon environnement de test 1", EnvModel.Status.VALID);
        var envModel2 = new EnvModel("test-env-2", "mon environnement de test 2", EnvModel.Status.VALID);
        var envModels = List.of(envModel1, envModel2);
        var encoded = codec.encode(envModels);

        assertNotNull(encoded);
        assertTrue(encoded.length > 0);

        var decoded = codec.decode(encoded);
        assertEquals(envModels, decoded);
    }

}
