package com.generic.retailer.trolley;

import com.generic.retailer.dto.Product;
import com.generic.retailer.dto.TrolleyItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class TrolleyImpl implements Trolley {

    private final Map<String, TrolleyItem> trolleyItems = new HashMap<>();

    /**
     * Adds a product to the trolley
     * @param product
     */
    @Override
    public void add(final Product product) {

        requireNonNull(product, "a null product cannot be added to the trolley");

        final String sku = product.getSku().toUpperCase();

        trolleyItems.putIfAbsent(sku, new TrolleyItem(product, 0));
        trolleyItems.computeIfPresent(sku.toUpperCase(), (key, trolleyItem) -> {
            int numOfItems = trolleyItem.getQuantity();
            trolleyItem.setQuantity(++numOfItems);
            return trolleyItem;
        } );
    }

    /**
     * Adds a list of items to the trolley
     * @param products
     */
    @Override
    public void addAll(final List<Product> products) {
        requireNonNull(products, "the list of products cannot be null");
        products.forEach( product -> this.add(product));
    }

    /**
     * reduces the quantity of the item in the trolley by one. if quantity drops to zero, the whole item is removed from the trolley
     * @param sku
     */
    @Override
    public void remove(final String sku) {
        TrolleyItem trolleyItem = trolleyItems.get(sku.toUpperCase());
        if (trolleyItem !=null ){
            int quantity = trolleyItem.getQuantity();
            quantity--;

            if (quantity <= 0 ){
                trolleyItems.remove(sku.toUpperCase());
            } else {
                trolleyItem.setQuantity(quantity);
            }
        }

    }

    /**
     * Empties the trolley
     */
    @Override
    public void emptyTrolley() {
        trolleyItems.clear();
    }

    /**
     * returns all items in the trolley
     * @return
     */
    @Override
    public Map<String, TrolleyItem> getTrolleyItems() {
        return trolleyItems;
    }
}
