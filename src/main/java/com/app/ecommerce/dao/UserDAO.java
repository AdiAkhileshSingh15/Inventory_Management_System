package com.app.ecommerce.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.app.ecommerce.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends PagingAndSortingRepository<User, Long>, ListCrudRepository<User, Long> {
    @Query("SELECT u.* FROM user u WHERE u.user_name=:userName")
    User findByUserName(@Param("userName") String userName);

    @Query("SELECT u.* FROM user u WHERE u.role=:role")
    List<User> findByRole(@Param("role") User.Role role);
}