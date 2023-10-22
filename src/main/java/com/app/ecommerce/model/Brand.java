package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

public record Brand(
        @Id Long brandId,
        String brandName) {
}
