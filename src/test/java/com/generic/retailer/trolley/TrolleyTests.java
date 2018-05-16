package com.generic.retailer.trolley;

import com.generic.retailer.dto.Book;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.Product;
import com.generic.retailer.dto.TrolleyItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class TrolleyTests {


    private  Trolley trolley;

    private final String DVD_SKU_1 = "DVD_SKU_1";
    private final String DVD_SKU_2 = "DVD_SKU_2";
    private final String BOOK_SKU_1 = "BOOK_SKU_1";
    private final String BOOK_SKU_2 = "BOOK_SKU_2";

    final DVD dvd1 =  DVD
            .builder()
            .runningTime(1000L)
            .desc("A really interesting Software dev movie")
            .sku(DVD_SKU_1)
            .price(new BigDecimal(15.00))
            .name("To kill a dev")

            .build();

    final Product dvd2 = DVD
            .builder()
            .desc("A really interesting Software dev movie Part 1")
            .sku(DVD_SKU_2)
            .price(new BigDecimal(15.00))
            .name("To kill a dev Part 2")
            .build();

    private final Book book1 = Book
            .builder()
            .desc("A really interesting Software dev book")
            .sku(BOOK_SKU_1)
            .price(new BigDecimal(5.00))
            .name("To become superstar dev")
            .build();

    private final Book book2 = Book
            .builder()
            .desc("A really interesting Software dev book V2")
            .sku(BOOK_SKU_2)
            .price(new BigDecimal(5.00))
            .name("To become superstar dev V2")
            .build();

    @Before
    public void setUp(){
        trolley = new TrolleyImpl();
    }

    @Test
    public void should_add_an_item_to_the_trolley(){

        trolley.add(dvd1);

        Map<String, TrolleyItem> actual = trolley.getTrolleyItems();
        assertThat(actual).hasSize(1);
        assertThat(actual).hasEntrySatisfying(DVD_SKU_1, (trolleyItem -> actual.containsValue(trolleyItem)));

    }

    @Test
    public void should_throw_NulPointerException_given_null_trolley_item_on_add_item(){

        final String errMsg = "a null product cannot be added to the trolley";
        assertThatThrownBy(() -> trolley.add(null)).hasMessage(errMsg);

    }

    @Test
    public void should_add_a_list_of_items_to_the_trolley(){

        trolley.addAll(Arrays.asList(dvd1, dvd2, book1, book2));

        Map<String, TrolleyItem> actual = trolley.getTrolleyItems();

        assertThat(actual).hasSize(4);

        assertThat(actual).hasEntrySatisfying(DVD_SKU_1, (trolleyItem -> actual.containsValue(trolleyItem)));
        assertThat(actual).hasEntrySatisfying(DVD_SKU_2, (trolleyItem -> actual.containsValue(trolleyItem)));
        assertThat(actual).hasEntrySatisfying(BOOK_SKU_1, (trolleyItem -> actual.containsValue(trolleyItem)));
        assertThat(actual).hasEntrySatisfying(BOOK_SKU_2, (trolleyItem -> actual.containsValue(trolleyItem)));

    }

    @Test
    public void should_remove_an_item_from_the_trolley_given_the_item_sku(){

        trolley.addAll(Arrays.asList(dvd1, dvd2, book1, book2));

        trolley.remove(DVD_SKU_1);
        trolley.remove(BOOK_SKU_1);

        Map<String, TrolleyItem> trolleyItems = trolley.getTrolleyItems();

        assertThat(trolleyItems).hasSize(2);

        assertThat(trolleyItems.get(DVD_SKU_2).getQuantity()).isEqualTo(1);
        assertThat(trolleyItems.get(DVD_SKU_2).getLineItem()).isEqualTo(dvd2);

        assertThat(trolleyItems.get(BOOK_SKU_2).getQuantity()).isEqualTo(1);
        assertThat(trolleyItems.get(BOOK_SKU_2).getLineItem()).isEqualTo(book2);
    }

    @Test
    public void should_empty_the_given_trolley(){

        trolley.addAll(Arrays.asList(dvd1, dvd2, book1, book2));
        trolley.emptyTrolley();
        assertThat(trolley.getTrolleyItems()).hasSize(0);

    }


}
