package com.app.ecommerce.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public record DateFilter(
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate) {
}
