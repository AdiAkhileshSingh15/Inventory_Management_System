package com.app.ecommerce.dao;

import com.app.ecommerce.model.TransactionDetails;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionDetailDAO
        extends PagingAndSortingRepository<TransactionDetails, Long>, ListCrudRepository<TransactionDetails, Long> {
    @Query("SELECT td.* FROM transaction_details td WHERE td.transaction=:transaction")
    List<TransactionDetails> findByTransaction(@Param("transaction") Long transaction);

    @Query("SELECT td.* FROM transaction_details td WHERE td.product=:product LIMIT :limit OFFSET :offset")
    List<TransactionDetails> filterByProduct(@Param("product") Long product, @Param("limit") int limit,
                                             @Param("offset") int offset);

    @Query("SELECT td.* FROM transaction_details td JOIN transaction t ON td.transaction = t.transaction_id WHERE td.product=:product AND t.tdate BETWEEN :sdate AND :edate LIMIT :limit OFFSET :offset")
    List<TransactionDetails> filterProductHistory(@Param("product") Long product, @Param("sdate") LocalDateTime sdate,
                                                  @Param("edate") LocalDateTime edate, @Param("limit") int limit, @Param("offset") int offset);

    @Modifying
    @Query("DELETE FROM transaction_details td WHERE td.transaction=:transaction")
    void deleteByTransaction(@Param("transaction") Long transaction);
}
