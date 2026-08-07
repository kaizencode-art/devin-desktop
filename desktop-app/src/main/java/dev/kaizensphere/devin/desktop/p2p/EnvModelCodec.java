package dev.kaizensphere.devin.desktop.p2p;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kaizensphere.devin.domain.model.EnvModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EnvModelCodec {

    private static final int MAX_PAYLOAD_SIZE = 1_000_000;

    private final ObjectMapper objectMapper;

    public EnvModelCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] encode(List<EnvModel> envModels) {
        Objects.requireNonNull(envModels);
        try{
            return objectMapper.writeValueAsBytes(envModels);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to encode environments", e);
        }
    }


    public List<EnvModel> decode(byte[] payload) {

        if(payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }

        if(payload.length > MAX_PAYLOAD_SIZE) {
            throw new IllegalArgumentException("Payload size exceeds maximum allowed size");
        }

        try {
            return objectMapper.readValue(payload, new TypeReference<List<EnvModel>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to decode payload", e);
        }
    }

}
