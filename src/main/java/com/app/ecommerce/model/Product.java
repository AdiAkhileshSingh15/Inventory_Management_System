package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record Product(
        @Id Long productId,
        @NotEmpty(message = "Please provide product name") String productName,
        @NotEmpty(message = "Please provide product description") String description,
        String type,
        @NotEmpty(message = "Please provide product category") String category,
        @NotEmpty(message = "Please provide subcategory") String subcategory,
        String colour,
        String dimension,
        Brand brand,
        Supplier supplier,
        @Valid Inventory inventory) {
}