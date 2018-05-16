package com.generic.retailer.discountbroker;

import com.generic.retailer.discountrules.DiscountRule;
import com.generic.retailer.dto.TrolleyItem;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * This Broker is used to apply discount rules to trolley items
 */
public class DiscountBrokerImpl implements DiscountBroker {

    @Override
    public BigDecimal applyBrokerRule(final TrolleyItemsFilter trolleyItemsFilter, final DiscountRule discountRule, final Map<String, TrolleyItem> trolleyItems) {

        requireNonNull(trolleyItemsFilter, "broker rule cannot be null");
        requireNonNull(discountRule, "discount rule cannot be null");

        Map<String, TrolleyItem> trolleyItemsToApplyDiscountRuleTo = trolleyItemsFilter.getFilter().apply(trolleyItems);
        if (trolleyItemsToApplyDiscountRuleTo != null){

            return discountRule.applyDiscountRule(trolleyItemsToApplyDiscountRuleTo);
        }

        return new BigDecimal(0).setScale(2, BigDecimal.ROUND_CEILING);

    }
}
