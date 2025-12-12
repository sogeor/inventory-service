package com.sogeor.service.inventory.service;

import com.sogeor.service.inventory.domain.Inventory;
import com.sogeor.service.inventory.dto.InventoryEvents;
import com.sogeor.service.inventory.messaging.InventoryEventProducer;
import com.sogeor.service.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final InventoryEventProducer eventProducer;

    public Mono<@NotNull Inventory> getInventory(UUID productId) {
        return inventoryRepository.findByProductId(productId)
                                  .switchIfEmpty(Mono.error(
                                          new RuntimeException("Inventory not found for product: " + productId)));
    }

    @Transactional
    public Mono<@NotNull Inventory> addStock(UUID productId, Integer quantity) {
        return inventoryRepository.findByProductId(productId)
                                  .defaultIfEmpty(
                                          Inventory.builder().productId(productId).quantity(0).reserved(0).build())
                                  .flatMap(inventory -> {
                                      inventory.setQuantity(inventory.getQuantity() + quantity);
                                      return inventoryRepository.save(inventory);
                                  })
                                  .doOnSuccess(this::sendUpdateEvent);
    }

    @Transactional
    public Mono<@NotNull Inventory> reserveStock(UUID productId, Integer quantity) {
        return inventoryRepository.findByProductId(productId)
                                  .switchIfEmpty(Mono.error(
                                          new RuntimeException("Inventory not found for product: " + productId)))
                                  .flatMap(inventory -> {
                                      if (inventory.getQuantity() - inventory.getReserved() < quantity) {
                                          return Mono.error(
                                                  new RuntimeException("Insufficient stock for product: " + productId));
                                      }
                                      inventory.setReserved(inventory.getReserved() + quantity);
                                      return inventoryRepository.save(inventory);
                                  })
                                  .doOnSuccess(this::sendUpdateEvent);
    }

    @Transactional
    public Mono<@NotNull Inventory> releaseStock(UUID productId, Integer quantity) {
        return inventoryRepository.findByProductId(productId)
                                  .switchIfEmpty(Mono.error(
                                          new RuntimeException("Inventory not found for product: " + productId)))
                                  .flatMap(inventory -> {
                                      if (inventory.getReserved() < quantity) {
                                          return Mono.error(new RuntimeException(
                                                  "Cannot release more than reserved for product: " + productId));
                                      }
                                      inventory.setReserved(inventory.getReserved() - quantity);
                                      return inventoryRepository.save(inventory);
                                  })
                                  .doOnSuccess(this::sendUpdateEvent);
    }

    @Transactional
    public Mono<@NotNull Inventory> deductStock(UUID productId, Integer quantity) {
        return inventoryRepository.findByProductId(productId)
                                  .switchIfEmpty(Mono.error(
                                          new RuntimeException("Inventory not found for product: " + productId)))
                                  .flatMap(inventory -> {
                                      int newQuantity = inventory.getQuantity() - quantity;

                                      if (newQuantity < 0) {
                                          return Mono.error(new RuntimeException(
                                                  "Insufficient stock to deduct for product: " + productId));
                                      }
                                      inventory.setQuantity(newQuantity);

                                      if (inventory.getReserved() >= quantity) {
                                          inventory.setReserved(inventory.getReserved() - quantity);
                                      }

                                      return inventoryRepository.save(inventory);
                                  })
                                  .doOnSuccess(this::sendUpdateEvent);
    }

    public Flux<@NotNull Inventory> getLowStockProducts(int threshold) {
        return inventoryRepository.findByQuantityLessThan(threshold);
    }

    public Mono<@NotNull Void> checkAndSendLowStockAlert(Inventory inventory, int threshold) {
        if (inventory.getQuantity() < threshold) {
            eventProducer.sendLowStockAlert(InventoryEvents.LowStockAlertEvent.builder()
                                                                              .eventType("LOW_STOCK")
                                                                              .productId(inventory.getProductId()
                                                                                                  .toString())
                                                                              .quantity(inventory.getQuantity())
                                                                              .threshold(threshold)
                                                                              .timestamp(Instant.now())
                                                                              .build());
        }
        return Mono.empty();
    }

    private void sendUpdateEvent(Inventory inventory) {
        eventProducer.sendInventoryUpdated(InventoryEvents.InventoryUpdatedEvent.builder()
                                                                                .eventType("INVENTORY_UPDATED")
                                                                                .productId(inventory.getProductId()
                                                                                                    .toString())
                                                                                .quantity(inventory.getQuantity())
                                                                                .reserved(inventory.getReserved())
                                                                                .timestamp(Instant.now())
                                                                                .build());
    }

}
