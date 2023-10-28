package com.app.ecommerce.dao;

import com.app.ecommerce.model.Inventory;
import org.springframework.data.repository.ListCrudRepository;

public interface InventoryDAO extends ListCrudRepository<Inventory, Long> {
}
