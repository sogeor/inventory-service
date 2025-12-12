package com.sogeor.service.inventory.controller;

import com.sogeor.service.inventory.dto.InventoryDto;
import com.sogeor.service.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public Mono<@NotNull ResponseEntity<InventoryDto.@NotNull InventoryResponse>> getInventory(
            @PathVariable UUID productId) {
        return inventoryService.getInventory(productId)
                               .map(inv -> ResponseEntity.ok(InventoryDto.InventoryResponse.builder()
                                                                                           .productId(
                                                                                                   inv.getProductId())
                                                                                           .quantity(inv.getQuantity())
                                                                                           .reserved(inv.getReserved())
                                                                                           .build()));
    }

    @PostMapping("/{productId}/add")
    public Mono<@NotNull ResponseEntity<InventoryDto.@NotNull InventoryResponse>> addStock(@PathVariable UUID productId,
                                                                                           @RequestBody InventoryDto.StockUpdateRequest request) {
        return inventoryService.addStock(productId, request.getQuantity())
                               .map(inv -> ResponseEntity.ok(InventoryDto.InventoryResponse.builder()
                                                                                           .productId(
                                                                                                   inv.getProductId())
                                                                                           .quantity(inv.getQuantity())
                                                                                           .reserved(inv.getReserved())
                                                                                           .build()));
    }

    @PutMapping("/{productId}/reserve")
    public Mono<@NotNull ResponseEntity<InventoryDto.@NotNull InventoryResponse>> reserveStock(
            @PathVariable UUID productId, @RequestBody InventoryDto.StockUpdateRequest request) {
        return inventoryService.reserveStock(productId, request.getQuantity())
                               .map(inv -> ResponseEntity.ok(InventoryDto.InventoryResponse.builder()
                                                                                           .productId(
                                                                                                   inv.getProductId())
                                                                                           .quantity(inv.getQuantity())
                                                                                           .reserved(inv.getReserved())
                                                                                           .build()));
    }

    @PutMapping("/{productId}/release")
    public Mono<@NotNull ResponseEntity<InventoryDto.@NotNull InventoryResponse>> releaseStock(
            @PathVariable UUID productId, @RequestBody InventoryDto.StockUpdateRequest request) {
        return inventoryService.releaseStock(productId, request.getQuantity())
                               .map(inv -> ResponseEntity.ok(InventoryDto.InventoryResponse.builder()
                                                                                           .productId(
                                                                                                   inv.getProductId())
                                                                                           .quantity(inv.getQuantity())
                                                                                           .reserved(inv.getReserved())
                                                                                           .build()));
    }

    @GetMapping("/low-stock")
    public Flux<InventoryDto.@NotNull InventoryResponse> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        return inventoryService.getLowStockProducts(threshold)
                               .map(inv -> InventoryDto.InventoryResponse.builder()
                                                                         .productId(inv.getProductId())
                                                                         .quantity(inv.getQuantity())
                                                                         .reserved(inv.getReserved())
                                                                         .build());
    }

}
