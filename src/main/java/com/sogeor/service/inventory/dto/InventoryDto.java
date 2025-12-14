package com.sogeor.service.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InventoryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryResponse {

        private String productId;

        private Integer quantity;

        private Integer reserved;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockUpdateRequest {

        private Integer quantity;

    }

}
