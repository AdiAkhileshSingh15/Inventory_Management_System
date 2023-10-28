package com.app.ecommerce.model;

import java.time.LocalDateTime;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.PastOrPresent;

import lombok.Data;

@Data
@NoArgsConstructor
public class Transaction {
    public enum TransactionType {
        RMA, PO, USAGE
    }

    @Id
    private long transactionId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    private LocalDateTime tdate;

    private TransactionType type;

    private Long user;

    private Long customer;

    public Transaction(LocalDateTime tdate, TransactionType type, Long user, Long customer) {
        this.tdate = tdate;
        this.type = type;
        this.user = user;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", tdate=" + tdate + ", type=" + type + ", user=" + user
                + "]";
    }
}