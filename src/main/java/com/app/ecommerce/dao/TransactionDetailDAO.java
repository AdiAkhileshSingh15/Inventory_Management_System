package com.app.ecommerce.dao;

import com.app.ecommerce.model.Product;
import com.app.ecommerce.model.Transaction;
import com.app.ecommerce.model.TransactionDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDetailDAO
        extends PagingAndSortingRepository<TransactionDetails, Long>, ListCrudRepository<TransactionDetails, Long> {
    List<TransactionDetails> findByTransaction(Transaction transaction);

    Page<TransactionDetails> findByProduct(Product product, Pageable pageable);

    Page<TransactionDetails> findByProductAndTransactionTdateBetween(Product product, LocalDateTime startdate,
            LocalDateTime endDate, Pageable pageable);

    void deleteByTransaction(Transaction transaction);
}
