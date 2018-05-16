package com.generic.retailer.discountbroker;

import com.generic.retailer.discountrules.DiscountRule;
import com.generic.retailer.dto.TrolleyItem;

import java.math.BigDecimal;
import java.util.Map;

/**
 * A broker that determines when discounts should be applied
 */
public interface DiscountBroker {

    /**
     * Applies the discount rule if the given predicate evaluates to true
     * @param trolleyItemsFilter
     * @param discountRule
     * @return
     */
    BigDecimal applyBrokerRule(final TrolleyItemsFilter trolleyItemsFilter, final DiscountRule discountRule, final Map<String, TrolleyItem> trolleyItems);
}
