package com.generic.retailer.discountbroker;

import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.TrolleyItem;
import com.rits.cloning.Cloner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * This filter is used to filter out trolley items to which a discount is applied to on a specific day
 * This is the filter that is used to filter out trolley items which are elegible for discounts on thurdsay
 */
public class DayOfWeekTrolleyItemsFilter implements TrolleyItemsFilter {

    private final DayOfWeek dayOfWeek;

    public DayOfWeekTrolleyItemsFilter(DayOfWeek dayOfWeek){

        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public Function<Map<String, TrolleyItem>, Map<String, TrolleyItem>> getFilter() {
        Function<Map<String, TrolleyItem>, Map<String, TrolleyItem>> givenDayBrokerRule = (trolleyItemMap) -> {
            requireNonNull(trolleyItemMap, "trolleyItems cannot be null");
            Cloner cloner = new Cloner();

            Map<String, TrolleyItem> filteredTrolleyToApplyThursdayDiscountsTo = new HashMap<>();

            if (trolleyItemMap != null && LocalDate.now().getDayOfWeek().equals(dayOfWeek)){
                filteredTrolleyToApplyThursdayDiscountsTo = trolleyItemMap.entrySet()
                        .stream()
                        .filter(entry -> !(entry.getValue().getLineItem() instanceof DVD))
                        .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

                Map<String, TrolleyItem> dvdTrolleyItems = trolleyItemMap.entrySet()
                        .stream()
                        .filter(entry -> (entry.getValue().getLineItem() instanceof DVD) && entry.getValue().getQuantity() % 2 == 1)
                        .map(entry -> {
                            TrolleyItem dvdTrolleyItem = cloner.deepClone(entry.getValue());
                            dvdTrolleyItem.setQuantity(1);
                            Map.Entry<String, TrolleyItem> newEntry =  new AbstractMap.SimpleEntry(entry.getKey(), dvdTrolleyItem);
                            return newEntry;
                        })
                        .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue() ));

                filteredTrolleyToApplyThursdayDiscountsTo.putAll(dvdTrolleyItems);
            }


            return filteredTrolleyToApplyThursdayDiscountsTo;
        };

        return givenDayBrokerRule;
    }
}
