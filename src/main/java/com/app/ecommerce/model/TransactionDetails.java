package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Embedded;

@Data
public class TransactionDetails {
    @Id
    private long Trans_Detail_Id;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
    private Transaction transaction;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
    private Product product;

    private int quantity;

    public TransactionDetails(Transaction transaction, Product product, int quantity) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TransactionDetails [Trans_Detail_Id=" + Trans_Detail_Id + ", product=" + product + ", quantity=" + quantity + "]";
    }
}