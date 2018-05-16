package com.generic.retailer.trolley;

import com.generic.retailer.dto.Product;
import com.generic.retailer.dto.TrolleyItem;

import java.util.List;
import java.util.Map;

public interface Trolley {

    /**
     * Adds a product to the trolley
     * @param product
     */
    void add(final Product product);

    /**
     * Adds a list of  products to the trolley
     * @param products
     */
    void addAll(final List<Product> products);

    /**
     * Removes an item from the trolley
     * @param sku
     */
    void remove(final String sku);

    /**
     * Empties the trolley
     */
    void emptyTrolley();

    /**
     *
     * Returns all the items in a trolley
     * @return
     */
    Map<String, TrolleyItem> getTrolleyItems();
}
