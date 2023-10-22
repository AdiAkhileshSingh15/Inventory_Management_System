package com.app.ecommerce.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Inventory(
        @Id Long inventoryId,
        @NotNull(message = "Field 'unitPrice' cannot be null") @Digits(integer = 7, fraction = 2, message = "fill in numbers, up to 2 d.p") Double unitPrice,
        @NotNull(message = "Field 'reorderLevel' cannot be null") @Digits(integer = 7, fraction = 0, message = "fill in numbers, up to 0 d.p") Integer reorderLevel,
        @NotNull(message = "Field 'wholesalePrice' cannot be null") @Positive(message = "fill in only positive numbers") @Digits(integer = 7, fraction = 2, message = "fill in numbers, up to 2 d.p") Double wholesalePrice,
        @NotNull(message = "Field 'partnerPrice' must be filled") @Positive(message = "fill in only positive numbers") @Digits(integer = 7, fraction = 2, message = "fill in numbers, up to 2 d.p") Double partnerPrice,
        @NotNull(message = "Field 'retailPrice' must be filled") @Positive(message = "fill in only postive numbers") @Digits(integer = 7, fraction = 2, message = "field cannot be empty, enter numbers up to 2 decimal place") Double retailPrice,
        @NotEmpty(message = "Please enter a valid storage location") String shelfLocation,
        @NotNull(message = "Field 'moq' must be filled") @Positive @Digits(integer = 7, message = "Please fill in the minimal order quantity required", fraction = 0) Integer moq,
        @Min(value = 0, message = "Please enter incoming quantity (must be more than 0)") Integer units,
        Boolean reorderFlag,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime lastEmailSent,
        Product product) {
}