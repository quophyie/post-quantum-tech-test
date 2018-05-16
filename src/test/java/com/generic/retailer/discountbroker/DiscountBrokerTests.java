package com.generic.retailer.discountbroker;

import com.generic.retailer.discountrules.DiscountRule;
import com.generic.retailer.discountrules.TwentyPercentOffDiscountRule;
import com.generic.retailer.dto.Book;
import com.generic.retailer.dto.CD;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.TrolleyItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class DiscountBrokerTests {

    private DiscountBroker discountBroker;
    private DiscountRule twentyPerecentDiscountRule;
    private final TrolleyItemsFilter trolleyItemsFilter = new DayOfWeekTrolleyItemsFilter(DayOfWeek.from(LocalDate.now()));

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
        twentyPerecentDiscountRule = new TwentyPercentOffDiscountRule();
        trolleyItems = new HashMap<>();
        dvdTrolleyItem = TrolleyItem.builder()
                .lineItem(dvd1)
                .quantity(3)
                .build();

        booksTrolleyItem = TrolleyItem.builder()
                .lineItem(book1)
                .quantity(2)
                .build();



        cdTrolleyItem = TrolleyItem.builder()
                .lineItem(cd1)
                .quantity(2)
                .build();

        trolleyItems.put(DVD_SKU_1, dvdTrolleyItem);
        trolleyItems.put(BOOK_SKU_1, booksTrolleyItem);
        trolleyItems.put(CD_SKU_1, cdTrolleyItem);

        discountBroker = new DiscountBrokerImpl();
    }

    @Test
    public void should_apply_discount_rule_given_that_the_predicate_evaluates_to_true(){

        final BigDecimal result = discountBroker.applyBrokerRule(trolleyItemsFilter, twentyPerecentDiscountRule, trolleyItems);

        assertThat(result).isEqualTo(new BigDecimal(9).setScale(2, BigDecimal.ROUND_CEILING));

    }


    @Test
    public void should_throw_NullPointerException_given_broker_rule(){

        assertThatThrownBy(() -> discountBroker.applyBrokerRule(null, twentyPerecentDiscountRule, trolleyItems)).hasMessage("broker rule cannot be null");
    }

    @Test
    public void should_throw_NullPointerException_given_discount_rule(){

        assertThatThrownBy(() -> discountBroker.applyBrokerRule(trolleyItemsFilter, null, trolleyItems)).hasMessage("discount rule cannot be null");
    }
}
