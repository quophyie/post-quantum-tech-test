package com.generic.retailer.inventory;

import com.generic.retailer.dto.Product;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository){

        this.inventoryRepository = inventoryRepository;
    }
    /**
     * Checks if the given product name is in stock
     * @return
     */
    @Override
    public boolean isInInventory(String productName) {
        requireNonNull(productName, "productName cannot be null");
        List<Product> products = inventoryRepository.findAll().orElse(Collections.EMPTY_LIST);
        return  products
                .stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName.trim()))
                .findFirst()
                .orElse(null) != null;
    }

    /**
     * Returns the given named product from the inventory
     * @param productName
     * @return
     */
    @Override
    public Optional<Product> getProduct(String productName) {
        return this.inventoryRepository.findProductByName(productName);
    }
}
