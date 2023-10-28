package com.app.ecommerce.dao;

import com.app.ecommerce.model.Transaction;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransactionDAO
        extends PagingAndSortingRepository<Transaction, Long>, ListCrudRepository<Transaction, Long> {
}