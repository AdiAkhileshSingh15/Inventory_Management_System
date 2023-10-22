package com.app.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.PastOrPresent;

public record Transaction(
        @Id Long transactionId,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PastOrPresent LocalDateTime tdate,
        Customer customer,
        User user,
        TransactionType type,
        List<TransactionDetails> td) {
    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", tdate=" + tdate + ", customer=" + customer + ", user="
                + user + ", type=" + type + "]";
    }
}
