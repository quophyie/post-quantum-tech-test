package com.generic.retailer.inventory;

import com.generic.retailer.dto.Book;
import com.generic.retailer.dto.CD;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.Product;
import org.w3c.dom.ls.LSInput;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The Inventory repository
 *
 * This actually a convenient implementation in the absence of a permanent datastore
 */
public class InventoryRepositoryImpl implements InventoryRepository {

    private List<Product> inventory;
    public InventoryRepositoryImpl(){
        final DVD dvd =  DVD
                .builder()
                .runningTime(1000L)
                .desc("A really interesting Software dev movie")
                .sku( "DVD_SKU")
                .price(new BigDecimal(15.00))
                .name("DVD")

                .build();

        final Product cd = CD
                .builder()
                .desc("Linux Distro")
                .sku("CD_SKU")
                .price(new BigDecimal(10.00))
                .name("CD")
                .size(2000L)
                .build();

        final Book book = Book
                .builder()
                .desc("A really interesting Software dev book")
                .sku("BOOK_SKU")
                .price(new BigDecimal(5.00))
                .name("BOOK")
                .synopsis("book synopis")
                .build();

        inventory = new ArrayList<>();
        inventory.add(dvd);
        inventory.add(cd);
        inventory.add(book);
    }

    /**
     * Returns a product given the product name
     * @param productName
     * @return
     */
    public Optional<Product> findProductByName(final String productName){

       return this.inventory
                .stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst();

    }

    @Override
    public Optional<List<Product>> findAll() {
        return Optional.of(inventory);
    }
}
