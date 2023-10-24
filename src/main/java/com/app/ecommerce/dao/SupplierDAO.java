package com.app.ecommerce.dao;

import com.app.ecommerce.model.Supplier;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SupplierDAO extends PagingAndSortingRepository<Supplier, Long>, ListCrudRepository<Supplier, Long> {
}
