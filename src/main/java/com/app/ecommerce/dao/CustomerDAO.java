package com.app.ecommerce.dao;

import com.app.ecommerce.model.Customer;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerDAO extends PagingAndSortingRepository<Customer, Long>, ListCrudRepository<Customer, Long> {
}
