package com.generic.retailer.checkoutmanager;

import com.generic.retailer.discountbroker.DayOfWeekTrolleyItemsFilter;
import com.generic.retailer.discountbroker.DiscountBroker;
import com.generic.retailer.discountbroker.Product2For1TrolleyItemsFilter;
import com.generic.retailer.discountbroker.TrolleyItemsFilter;
import com.generic.retailer.discountrules.DiscountRule;
import com.generic.retailer.discountrules.TwentyPercentOffDiscountRule;
import com.generic.retailer.discountrules.TwoForOneDiscountRule;
import com.generic.retailer.dto.Book;
import com.generic.retailer.dto.CD;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.Product;
import com.generic.retailer.dto.Receipt;
import com.generic.retailer.dto.ReceiptLine;
import com.generic.retailer.dto.TrolleyItem;
import com.generic.retailer.trolley.Trolley;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutServiceTests {

    private CheckoutService checkoutService;
    private Receipt receipt;
    private Map<String, TrolleyItem> trolleyItems;
    private TrolleyItem dvdTrolleyItems, cdTrolleyItems, bookTrolleyItems;
    private Map<TrolleyItemsFilter, LinkedHashMap<String, DiscountRule>> trolleyItemsFiltersToDiscountRuleMap;


    @Mock
    private Trolley trolley;

    @Mock
    private DiscountBroker discountBroker;


    @Before
    public void setUp(){


        trolleyItemsFiltersToDiscountRuleMap = new HashMap<>();
        LinkedHashMap twoForDiscountRuleMap = new LinkedHashMap();
        twoForDiscountRuleMap.put("2 FOR 1", new TwoForOneDiscountRule());

        LinkedHashMap thursdayDiscountRuleMap = new LinkedHashMap();
        thursdayDiscountRuleMap.put("THURS", new TwentyPercentOffDiscountRule());
        trolleyItemsFiltersToDiscountRuleMap.put(new Product2For1TrolleyItemsFilter<>(DVD.class), twoForDiscountRuleMap);
        trolleyItemsFiltersToDiscountRuleMap.put(new DayOfWeekTrolleyItemsFilter(DayOfWeek.from(LocalDate.now())), thursdayDiscountRuleMap);

        final String DVD_SKU = "DVD_SKU";
        final String CD_SKU = "CD_SKU";
        final String BOOK_SKU = "BOOK_SKU";
        trolleyItems = new HashMap<>();

        final DVD dvd =  DVD
                .builder()
                .runningTime(1000L)
                .desc("A really interesting Software dev movie")
                .sku( DVD_SKU)
                .price(new BigDecimal(15.00))
                .name("DVD")

                .build();

        final Product cd = CD
                .builder()
                .desc("Linux Distro")
                .sku(CD_SKU)
                .price(new BigDecimal(10.00))
                .name("CD")
                .size(2000L)
                .build();

         final Book book = Book
                .builder()
                .desc("A really interesting Software dev book")
                .sku(BOOK_SKU)
                .price(new BigDecimal(5.00))
                .name("To become superstar dev")
                .synopsis("book synopis")
                .build();

        dvdTrolleyItems =  TrolleyItem.builder().lineItem(dvd).quantity(3).build();
        cdTrolleyItems = TrolleyItem.builder().lineItem(cd).quantity(2).build();
        bookTrolleyItems = TrolleyItem.builder().lineItem(book).quantity(4).build();

        trolleyItems.put(DVD_SKU, dvdTrolleyItems);
        trolleyItems.put(CD_SKU, cdTrolleyItems);
        trolleyItems.put(BOOK_SKU, bookTrolleyItems);


        checkoutService = new CheckoutServiceImpl(discountBroker, trolleyItemsFiltersToDiscountRuleMap);
        List<ReceiptLine> receiptLines = Arrays.asList(
                ReceiptLine.builder()
                        .amount(BigDecimal.valueOf(30.00))
                        .receiptItemName("DVD (X2)")
                        .build(),
                ReceiptLine.builder()
                        .amount(BigDecimal.valueOf(20.00))
                        .receiptItemName("CD (X2)")
                        .build(),
                ReceiptLine.builder()
                        .amount(BigDecimal.valueOf(-15.00))
                        .receiptItemName("2 FOR 1")
                        .build(),
                ReceiptLine.builder()
                        .amount(BigDecimal.valueOf(-4.00))
                        .receiptItemName("THURS")
                        .build()

        );
        receipt = Receipt
                .builder()
                .receiptLines(receiptLines)
                .total(BigDecimal.valueOf(31))
                .build();
    }

    @After
    public void tearDown(){
        reset(trolley, discountBroker);
    }

    @Test
    public void should_thrown_NullPointerException_given_null_receipt(){
        assertThatThrownBy(() -> checkoutService.getReceiptAsString(null)).hasMessage("receipt cannot be null");
    }

    @Test
    public void should_thrown_NullPointerException_given_null_receipt_lines(){
        assertThatThrownBy(() -> checkoutService.getReceiptAsString(Receipt.builder().build())).hasMessage("receipt.receiptLines cannot be null");
    }

    @Test
    public void should_get_the_reciept_to_print_as_a_string(){
        String expected =
                "===== RECEIPT ======\n" +
                "DVD (X2)      £30.00\n" +
                "CD (X2)       £20.00\n" +
                "2 FOR 1      -£15.00\n" +
                "THURS         -£4.00\n" +
                "====================\n" +
                "TOTAL         £31.00";
        final String receiptAsStr = checkoutService.getReceiptAsString(receipt);
        assertThat(receiptAsStr).isEqualTo(expected);
    }

    @Test
    public void should_build_a_reciept_given_a_trolley_with_items_in_it(){
        Map<String, TrolleyItem> items = new HashMap<>();
        items.putAll(trolleyItems);

        given(trolley.getTrolleyItems()).willReturn(trolleyItems);
        given(discountBroker.applyBrokerRule(any(Product2For1TrolleyItemsFilter.class), any(TwoForOneDiscountRule.class), ArgumentMatchers.anyMap())
        ).willReturn(BigDecimal.valueOf(15));

        given(discountBroker.applyBrokerRule(any(DayOfWeekTrolleyItemsFilter.class), any(TwentyPercentOffDiscountRule.class), ArgumentMatchers.anyMap())
        ).willReturn(BigDecimal.valueOf(11));

        Receipt receipt = checkoutService.buildReciept(trolley);
        assertThat(receipt.getReceiptLines().size()).isEqualTo(5);
        assertThat(receipt.getTotal()).isEqualTo(BigDecimal.valueOf(59));
    }


}
