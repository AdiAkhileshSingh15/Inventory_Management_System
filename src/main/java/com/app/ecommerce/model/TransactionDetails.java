package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class TransactionDetails {

    @Id
    private long Trans_Detail_Id;

    private Transaction transaction;

    private Product product;

    private int quantity;

    public TransactionDetails(Transaction transaction, Product product, int quantity) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TransactionDetails [Trans_Detail_Id=" + Trans_Detail_Id + ", product="
                + product + ", quantity=" + quantity + "]";
    }
}