package com.finance.finmailing.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.finmailing.dto.MessageDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.messaging.support.GenericMessage;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@Slf4j
public class MessageDeserializer implements Deserializer<MessageDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MessageDTO deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            log.info("No data present to deserialize");
            return null;
        }
        try {
            GenericMessage<String> message = objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), new TypeReference<>() {
            });
            log.info("Parsed from generic message... {}", message.getPayload());
            return objectMapper.readValue(message.getPayload(), MessageDTO.class);
        } catch (Exception e) {
            throw new SerializationException("");
        }
    }
}
