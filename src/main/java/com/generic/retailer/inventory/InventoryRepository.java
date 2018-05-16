package com.generic.retailer.inventory;

import com.generic.retailer.dto.Product;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    /**
     * Returns a product given the product name
     * @param productName
     * @return
     */
    Optional<Product> findProductByName(final String productName);

    /**
     * Returns all items in the inventory
     * @return
     */
    Optional<List<Product>> findAll();
}
