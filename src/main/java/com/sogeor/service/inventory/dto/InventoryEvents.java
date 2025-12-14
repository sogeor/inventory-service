package com.sogeor.service.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class InventoryEvents {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryUpdatedEvent {

        private String eventType;

        private String productId;

        private Integer quantity;

        private Integer reserved;

        private Instant timestamp;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LowStockAlertEvent {

        private String eventType;

        private String productId;

        private Integer quantity;

        private Integer threshold;

        private Instant timestamp;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductUpdatedEvent {

        private String eventType;

        private String productId;

        private String name;

        private BigDecimal price;

        private String category;

        private Instant timestamp;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderEvent {

        private String eventType;

        private String orderId;

        private List<OrderItem> items;

        private Instant timestamp;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {

        private String productId;

        private Integer quantity;

    }

}
