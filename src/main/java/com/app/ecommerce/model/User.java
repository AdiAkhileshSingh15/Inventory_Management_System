package com.app.ecommerce.model;

import javax.management.relation.Role;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record User(
        @Id Long userID,
        @NotEmpty(message = "Please provide a username") String userName,
        @NotEmpty(message = "Please provide a password") @Size(min = 8, max = 32, message = "Your password must be between 8 and 32 characters") String password,
        @NotEmpty(message = "Please provide your full name") String name,
        @Email String email,
        Long phone,
        @NotNull(message = "Please annotate a role") Role role) {
}