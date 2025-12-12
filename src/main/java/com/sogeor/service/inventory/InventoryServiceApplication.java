package com.sogeor.service.inventory;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @since 1.0.0-RC1
 */
@OpenAPIDefinition(servers = @Server(url = "https://api.sogeor.com/v1/inventory"))
@SpringBootApplication
public class InventoryServiceApplication {

    /**
     * @since 1.0.0-RC1
     */
    static void main(final @NotNull String... arguments) {
        SpringApplication.run(InventoryServiceApplication.class, arguments);
    }

}
