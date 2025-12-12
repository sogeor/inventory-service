package com.sogeor.service.inventory.service;

import com.sogeor.service.inventory.domain.Inventory;
import com.sogeor.service.inventory.messaging.InventoryEventProducer;
import com.sogeor.service.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryEventProducer eventProducer;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void addStock_shouldCreateNewInventory_whenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();
        Inventory newInventory = Inventory.builder().productId(productId).quantity(10).reserved(0).build();

        when(inventoryRepository.findByProductId(productId)).thenReturn(Mono.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(Mono.just(newInventory));

        StepVerifier.create(inventoryService.addStock(productId, 10))
                    .expectNextMatches(inv -> inv.getQuantity() == 10 && inv.getProductId().equals(productId))
                    .verifyComplete();

        verify(eventProducer).sendInventoryUpdated(any());
    }

    @Test
    void addStock_shouldUpdateExistingInventory_whenProductExists() {
        UUID productId = UUID.randomUUID();
        Inventory existingInventory = Inventory.builder().productId(productId).quantity(5).reserved(0).build();

        Inventory updatedInventory = Inventory.builder().productId(productId).quantity(15).reserved(0).build();

        when(inventoryRepository.findByProductId(productId)).thenReturn(Mono.just(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(Mono.just(updatedInventory));

        StepVerifier.create(inventoryService.addStock(productId, 10))
                    .expectNextMatches(inv -> inv.getQuantity() == 15)
                    .verifyComplete();

        verify(eventProducer).sendInventoryUpdated(any());
    }

    @Test
    void reserveStock_shouldReserve_whenSufficientStock() {
        UUID productId = UUID.randomUUID();
        Inventory existingInventory = Inventory.builder().productId(productId).quantity(10).reserved(0).build();

        Inventory reservedInventory = Inventory.builder().productId(productId).quantity(10).reserved(5).build();

        when(inventoryRepository.findByProductId(productId)).thenReturn(Mono.just(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(Mono.just(reservedInventory));

        StepVerifier.create(inventoryService.reserveStock(productId, 5))
                    .expectNextMatches(inv -> inv.getReserved() == 5)
                    .verifyComplete();
    }

    @Test
    void reserveStock_shouldError_whenInsufficientStock() {
        UUID productId = UUID.randomUUID();
        Inventory existingInventory = Inventory.builder().productId(productId).quantity(10).reserved(8).build();

        when(inventoryRepository.findByProductId(productId)).thenReturn(Mono.just(existingInventory));

        StepVerifier.create(inventoryService.reserveStock(productId, 5)) // 10-8=2 available, need 5
                    .expectError(RuntimeException.class).verify();
    }

}
