package com.app.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.PastOrPresent;

import lombok.Data;

@Data
public class Transaction {
    public enum TransactionType {
        RMA, PO, USAGE
    }

    @Id
    private long transactionId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent
    private LocalDateTime tdate;

    private Customer customer;

    private User user;
    private TransactionType type;

    private List<TransactionDetails> td;

    public Transaction(LocalDateTime tdate, Customer customer, User user, TransactionType type, List<TransactionDetails> td) {
        this.tdate = tdate;
        this.customer = customer;
        this.user = user;
        this.type = type;
        this.td = td;
    }

    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", tdate=" + tdate + ", customer=" + customer + ", user=" + user + ", type=" + type + "]";
    }
}