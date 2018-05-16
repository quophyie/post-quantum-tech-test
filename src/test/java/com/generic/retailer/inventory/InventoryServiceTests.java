package com.generic.retailer.inventory;

import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InventoryServiceTests {

    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    private final String DVD_SKU_1 = "DVD_SKU_1";
    private final String DVD_SKU_2 = "DVD_SKU_2";
    private final String BOOK_SKU_1 = "BOOK_SKU_1";

    private List<Product> inventory;

    final DVD dvd1 = DVD
            .builder()
            .desc("A really interesting Software dev movie")
            .sku(DVD_SKU_1)
            .price(new BigDecimal(15.00))
            .name("To kill a dev")
            .build();

    final DVD dvd2 = DVD
            .builder()
            .desc("A really interesting Software dev movie Part 1")
            .sku(DVD_SKU_2)
            .price(new BigDecimal(15.00))
            .name("To kill a dev Part 2")
            .build();

    private final DVD book1 = DVD
            .builder()
            .desc("A really interesting Software dev book")
            .sku(BOOK_SKU_1)
            .price(new BigDecimal(5.00))
            .name("To become superstar dev")
            .build();

    @Before
    public void setUp(){
        inventory = new ArrayList<>();
        inventory.add(dvd1);
        inventory.add(dvd2);
        inventory.add(book1);
        inventoryService = new InventoryServiceImpl(inventoryRepository);
    }

    @After
    public void tearDown(){
        Mockito.reset(inventoryRepository);
    }

    @Test
    public void should_return_true_given_a_product_that_exists_in_inventory(){

        final String productName = "To kill a dev";
        given(inventoryRepository.findAll()).willReturn(Optional.of(inventory));
        assertThat(inventoryService.isInInventory(productName)).isTrue();

        verify(inventoryRepository).findAll();

    }

    @Test
    public void should_return_false_given_a_product_that_does_not_exists_in_inventory(){

        final String productName = "NOT_EXISTS";
        given(inventoryRepository.findAll()).willReturn(Optional.of(inventory));
        assertThat(inventoryService.isInInventory(productName)).isFalse();

        verify(inventoryRepository).findAll();

    }

    @Test
    public void should_return_a_product_given_a_product_name_that_exists_in_inventory(){

        final String productName = "To kill a dev";

        given(inventoryRepository.findProductByName(productName)).willReturn(Optional.of(dvd1));
        Product product = inventoryService.getProduct(productName).get();
        assertThat(product).isEqualTo(dvd1);

    }

    @Test
    public void should_return_null_given_a_product_that_does_not_in_inventory(){

        final String productName = "non_existent";
        given(inventoryRepository.findProductByName(productName)).willReturn(Optional.empty());
        Optional<Product> product = inventoryService.getProduct(productName);
        assertThat(product).isEqualTo(Optional.empty());

    }


}
