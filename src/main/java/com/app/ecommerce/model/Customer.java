package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record Customer(
        @Id Long customerID,
        @NotEmpty(message = "Please provide a customer name") String customerName,
        @NotEmpty String vehicle,
        @Email(message = "Please provide a valid email address") String email,
        @NotEmpty(message = "Please provide a phone number") @Size(min = 10, max = 10, message = "Please provide a valid phone number (10-digits)") String phone) {
}