package com.generic.retailer.discountbroker;

import com.generic.retailer.dto.TrolleyItem;

import java.util.Map;
import java.util.function.Function;

public interface TrolleyItemsFilter {

    /**
     * A Filtter that returns a function that which takes a map of trolley items and returns a new map of trolley items that price rule may be applied to
     * @return
     */
    Function<Map<String, TrolleyItem>, Map<String, TrolleyItem>> getFilter();
}
