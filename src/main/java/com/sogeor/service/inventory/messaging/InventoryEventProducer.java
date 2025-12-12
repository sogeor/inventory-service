package com.sogeor.service.inventory.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeor.service.inventory.dto.InventoryEvents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    private static final String TOPIC = "inventory-updates";

    private final KafkaTemplate<@NotNull String, @NotNull String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public void sendInventoryUpdated(InventoryEvents.InventoryUpdatedEvent event) {
        sendEvent(event.getProductId(), event);
    }

    public void sendLowStockAlert(InventoryEvents.LowStockAlertEvent event) {
        sendEvent(event.getProductId(), event);
    }

    private void sendEvent(String key, Object event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, key, payload).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send event to topic {}: {}", TOPIC, ex.getMessage());
                } else {
                    log.debug("Event sent to topic {} successfully", TOPIC);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Error serializing event", e);
        }
    }

}
