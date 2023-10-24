package com.app.ecommerce.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@NoArgsConstructor
@Data
public class Supplier {
    @Id
    private Long supplierId;
    @NotEmpty(message = "name field cannot be empty")
    private String name;
    @NotEmpty(message = "address cannot be empty")
    private String address;
    @NotEmpty(message = "Phone number cannot be empty")
    private String phone;
    @NotEmpty(message = "Email address cannot be empty")
    private String email;

    public Supplier(String name, String address, String email, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}