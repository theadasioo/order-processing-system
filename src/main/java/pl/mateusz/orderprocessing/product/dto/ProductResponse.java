package pl.mateusz.orderprocessing.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse (
        Long id,
        String name,
        BigDecimal price,
        String description,
        LocalDateTime createdAt
) {
}
