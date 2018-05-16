package com.generic.retailer.inventory;

import com.generic.retailer.dto.Product;

import java.util.Optional;

public interface InventoryService {

    /**
     * Checks if the given product name is in stock
     * @return
     */
    boolean isInInventory(String productName);


    /**
     * Returns the product with given name
     * @param name
     * @return
     */
    Optional<Product> getProduct(String name);
}
