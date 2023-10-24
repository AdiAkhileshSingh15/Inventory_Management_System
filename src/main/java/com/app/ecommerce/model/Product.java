package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class Product {
    @Id
    private Long productId;
    @NotEmpty(message = "Field 'product name' cannot be empty")
    private String productName;
    @NotEmpty(message = "Field 'description' cannot be empty")
    private String description;
    private String type;
    @NotEmpty(message = "Field 'category' cannot be empty")
    private String category;
    @NotEmpty(message = "Field 'subcategory' cannot be empty")
    private String subcategory;
    private String colour;
    private String dimenision;

    private Brand brand;
    private Supplier supplier;

    @Valid
    private Inventory inventory;

    public Product(String productName, String description, String type, String category, String subcategory, String colour, String dimenision, Brand brand, Supplier supplier, Inventory inventory) {
        this.productName = productName;
        this.description = description;
        this.type = type;
        this.category = category;
        this.subcategory = subcategory;
        this.colour = colour;
        this.dimenision = dimenision;
        this.brand = brand;
        this.supplier = supplier;
        this.inventory = inventory;
    }
}