package com.generic.retailer.discountrules;

import com.generic.retailer.dto.Book;
import com.generic.retailer.dto.CD;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.TrolleyItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TwoForOneDiscountRuleTests {

    private  DiscountRule discountRule;
    private Map<String, TrolleyItem> trolleyItems;


    private final String DVD_SKU_1 = "DVD_SKU_1";
    private final String BOOK_SKU_1 = "BOOK_SKU_1";
    private final String CD_SKU_1 = "CD_SKU_1";

    private TrolleyItem dvdTrolleyItem, booksTrolleyItem, cdTrolleyItem;
    final DVD dvd1 = DVD
            .builder()
            .desc("A really interesting Software dev movie")
            .sku(DVD_SKU_1)
            .price(new BigDecimal(15.00))
            .name("To kill a dev")
            .runningTime(20000L)
            .build();

    private final Book book1 = Book
            .builder()
            .desc("A really interesting Software dev book")
            .sku(BOOK_SKU_1)
            .price(new BigDecimal(5.00))
            .name("To become superstar dev")
            .synopsis("Book1 synopsis")
            .build();


    final CD cd1 = CD
            .builder()
            .desc("Linux installation cd")
            .sku(CD_SKU_1)
            .price(new BigDecimal(10.00))
            .name("Linux Installer")
            .size(40000L)
            .build();


    @Before
    public void setUp(){
        discountRule = new TwoForOneDiscountRule();
        trolleyItems = new HashMap<>();
        dvdTrolleyItem = TrolleyItem.builder()
                .lineItem(dvd1)
                .quantity(3)
                .build();

        booksTrolleyItem = TrolleyItem.builder()
                .lineItem(book1)
                .quantity(3)
                .build();



        cdTrolleyItem = TrolleyItem.builder()
                .lineItem(cd1)
                .quantity(1)
                .build();

        trolleyItems.put(DVD_SKU_1, dvdTrolleyItem);
        trolleyItems.put(BOOK_SKU_1, booksTrolleyItem);
        trolleyItems.put(CD_SKU_1, cdTrolleyItem);

    }

    @Test
    public void should_apply_2_for_1_discount_rule_given_trolley_items(){

        BigDecimal total = discountRule.applyDiscountRule(trolleyItems);
        assertThat(total).isEqualTo(new BigDecimal(20));
    }

    @Test
    public void should_not_apply_2_for_1_discount_rule_given_each_trolley_item_has_a_quantity_of_one(){

        dvdTrolleyItem.setQuantity(1);
        booksTrolleyItem.setQuantity(1);
        cdTrolleyItem.setQuantity(1);
        BigDecimal total = discountRule.applyDiscountRule(trolleyItems);
        assertThat(total).isEqualTo(new BigDecimal(0));
    }
}
