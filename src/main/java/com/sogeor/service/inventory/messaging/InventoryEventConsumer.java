package com.sogeor.service.inventory.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeor.service.inventory.dto.InventoryEvents;
import com.sogeor.service.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final InventoryService inventoryService;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "product-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductUpdates(String message) {
        try {
            InventoryEvents.ProductUpdatedEvent event = objectMapper.readValue(message,
                                                                               InventoryEvents.ProductUpdatedEvent.class);
            log.info("Received Product Update for: {}", event.getProductId());
            inventoryService.addStock(UUID.fromString(event.getProductId()), 0).subscribe();
        } catch (JsonProcessingException e) {
            log.error("Error processing product update", e);
        }
    }

    @KafkaListener(topics = "order-events", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderEvents(String message) {
        try {
            InventoryEvents.OrderEvent event = objectMapper.readValue(message, InventoryEvents.OrderEvent.class);

            if ("ORDER_PAID".equals(event.getEventType())) {
                log.info("Processing ORDER_PAID for order: {}", event.getOrderId());
                event.getItems()
                     .forEach(item -> inventoryService.deductStock(UUID.fromString(item.getProductId()),
                                                                   item.getQuantity())
                                                      .subscribe(result -> log.debug("Stock deducted for product {}",
                                                                                     item.getProductId()),
                                                                 error -> log.error(
                                                                         "Failed to deduct stock for product {}: {}",
                                                                         item.getProductId(), error.getMessage())));
            } else if ("ORDER_CANCELLED".equals(event.getEventType())) {
                log.info("Processing ORDER_CANCELLED for order: {}", event.getOrderId());
                event.getItems()
                     .forEach(
                             item -> inventoryService.addStock(UUID.fromString(item.getProductId()), item.getQuantity())
                                                     .subscribe(result -> log.debug("Stock returned for product {}",
                                                                                    item.getProductId()),
                                                                error -> log.error(
                                                                        "Failed to return stock for product {}: {}",
                                                                        item.getProductId(), error.getMessage())));
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing order event", e);
        }
    }

}
