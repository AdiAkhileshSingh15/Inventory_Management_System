package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotEmpty;

public record Supplier(
        @Id Long supplierId,
        @NotEmpty(message = "name field cannot be empty") String name,
        @NotEmpty(message = "address cannot be empty") String address,
        @NotEmpty(message = "phone number cannot be empty") String phone,
        @NotEmpty(message = "email address cannot be empty") String email) {
}
