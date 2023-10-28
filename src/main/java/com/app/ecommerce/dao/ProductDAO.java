package com.app.ecommerce.dao;

import com.app.ecommerce.model.Product;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDAO extends PagingAndSortingRepository<Product, Long>, ListCrudRepository<Product, Long> {
    @Query("SELECT DISTINCT type FROM product")
    List<String> getTypes();

    @Query("SELECT DISTINCT category FROM product")
    List<String> getCategories();

    @Query("SELECT DISTINCT subcategory FROM product")
    List<String> getSubcategories();

    @Query("SELECT DISTINCT colour FROM product")
    List<String> getColours();

    @Query("SELECT p.* FROM product p JOIN inventory i ON p.inventory = i.inventory_id WHERE i.units<i.reorder_level AND p.supplier=:sid")
    List<Product> findReorderProducts(@Param("sid") long sid);
}
