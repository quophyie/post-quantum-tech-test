package com.generic.retailer.inventory;

import com.generic.retailer.dto.Book;
import com.generic.retailer.dto.CD;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InventoryRepositoryTests {

    private InventoryRepository inventoryRepository;

    private final DVD dvd =  DVD
            .builder()
            .runningTime(1000L)
            .desc("A really interesting Software dev movie")
            .sku( "DVD_SKU")
            .price(new BigDecimal(15.00))
            .name("DVD")

            .build();

    private final Product cd = CD
            .builder()
            .desc("Linux Distro")
            .sku("CD_SKU")
            .price(new BigDecimal(10.00))
            .name("CD")
            .size(2000L)
            .build();

    private final Book book = Book
            .builder()
            .desc("A really interesting Software dev book")
            .sku("BOOK_SKU")
            .price(new BigDecimal(5.00))
            .name("BOOK")
            .synopsis("book synopis")
            .build();


    @Before
    public void setUp(){
        inventoryRepository = new InventoryRepositoryImpl();
    }


    @Test
    public void should_return_all_products_in_the_inventory(){
        List<Product> products = inventoryRepository.findAll().get();

        assertThat(products).contains(cd, dvd, book);
    }

    @Test
    public void should_return_a_product_by_name_given_the_product_exists_in_the_inventory(){
        Product bookInInventory = inventoryRepository.findProductByName("BOOK").get();

        assertThat(bookInInventory).isNotNull();
        assertThat(bookInInventory).isEqualTo(book);
    }

    @Test
    public void should_return_an_empty_optional_given_the_product_exists_in_the_inventory(){
        Optional<Product> bookInInventory = inventoryRepository.findProductByName("NOT_EXISTS");

        assertThat(bookInInventory).isEqualTo(Optional.empty());
    }
}
