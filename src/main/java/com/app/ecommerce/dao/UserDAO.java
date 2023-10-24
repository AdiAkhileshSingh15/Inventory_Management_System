package com.app.ecommerce.dao;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.app.ecommerce.model.User;

public interface UserDAO extends PagingAndSortingRepository<User, Long>, ListCrudRepository<User, Long> {
    User findByUserName(String username);
}