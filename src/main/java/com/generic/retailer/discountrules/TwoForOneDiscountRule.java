package com.generic.retailer.discountrules;

import com.generic.retailer.dto.TrolleyItem;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The 2 for 1 discount rule
 */
public class TwoForOneDiscountRule implements DiscountRule {
    @Override
    public BigDecimal applyDiscountRule(final  Map<String, TrolleyItem> trolleyItems) {

        return trolleyItems
                .keySet()
                .stream()
                .map(key ->{
                    TrolleyItem trolleyItem = trolleyItems.get(key);
                    BigDecimal discountedAmount = new BigDecimal(0);
                    if (trolleyItem != null){
                        final int numOfProductsToApplyDiscountTo = trolleyItem.getQuantity() / 2;
                        discountedAmount = trolleyItem.getLineItem().getPrice().multiply(BigDecimal.valueOf(numOfProductsToApplyDiscountTo));
                    }

                    return discountedAmount;
                })
                .reduce((prev, current) -> prev.add(current))
                .orElse(new BigDecimal(0));

    }
}
