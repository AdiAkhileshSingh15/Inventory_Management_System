package com.app.ecommerce.model;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import lombok.Data;

@NoArgsConstructor
@Data
public class TransactionDetails {
    @Id
    private long transDetailId;

    private Long transaction;

    private Long product;

    private int quantity;

    public TransactionDetails(Long transaction, Long product, int quantity) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TransactionDetails [Trans_Detail_Id=" + transDetailId + ", product=" + product + ", quantity=" + quantity + "]";
    }
}