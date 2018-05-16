package com.generic.retailer.discountbroker;

import com.generic.retailer.dto.Product;
import com.generic.retailer.dto.TrolleyItem;

import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

/**
 * THis filter is used to filter for trolley items to which a 2 for 1 discount rule will be applied
 * @param <T>
 */
public class Product2For1TrolleyItemsFilter<T extends Product> implements TrolleyItemsFilter {

    private final Class<T> productType;

    public Product2For1TrolleyItemsFilter(Class<T> productType){

        this.productType = productType;
    }
    @Override
    public Function<Map<String, TrolleyItem>, Map<String, TrolleyItem>> getFilter() {

        final Function<Map<String, TrolleyItem>, Map<String, TrolleyItem>> twoForOneBrokerRuleFunc = (Map<String, TrolleyItem> trolleyItems)  -> {

            requireNonNull(trolleyItems, "trolleyItems cannot be null");

            Map<String, TrolleyItem> filterTrolleyItems = trolleyItems.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getLineItem().getClass() == productType)
                    .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));
            return filterTrolleyItems;
        };

        return twoForOneBrokerRuleFunc;
    }
}
