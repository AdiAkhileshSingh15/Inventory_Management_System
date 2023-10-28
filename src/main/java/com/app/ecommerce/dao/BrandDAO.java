package com.app.ecommerce.dao;

import com.app.ecommerce.model.Brand;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandDAO extends ListCrudRepository<Brand, Long> {
    @Query("SELECT b.* FROM brand b WHERE b.brand_name=:brandName")
    Brand findByBrandName(@Param("brandName") String brandName);

    @Query("SELECT b.brand_name FROM brand b")
    List<String> getAllBrandNames();
}
