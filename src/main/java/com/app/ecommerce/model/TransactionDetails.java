package com.app.ecommerce.model;

import org.springframework.data.annotation.Id;

public record TransactionDetails(
        @Id Long Trans_Detail_Id,
        Transaction transaction,
        Product product,
        Integer quantity) {
    @Override
    public String toString() {
        return "TransactionDetails [Trans_Detail_Id=" + Trans_Detail_Id + ", product="
                + product + ", quantity=" + quantity + "]";
    }
}
