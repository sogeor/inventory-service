package com.sogeor.service.inventory.repository;

import com.sogeor.service.inventory.domain.Inventory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface InventoryRepository extends R2dbcRepository<@NotNull Inventory, @NotNull Integer> {

    Mono<@NotNull Inventory> findByProductId(UUID productId);

    Flux<@NotNull Inventory> findByQuantityLessThan(int threshold);

}
