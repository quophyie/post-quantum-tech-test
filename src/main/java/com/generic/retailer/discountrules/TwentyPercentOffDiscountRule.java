package com.generic.retailer.discountrules;

import com.generic.retailer.dto.TrolleyItem;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 20% off discount rule
 */
public class TwentyPercentOffDiscountRule implements DiscountRule {

    private final BigDecimal ZERO_POINT_TWO = new BigDecimal(20).divide(new BigDecimal(100));

    @Override
    public BigDecimal applyDiscountRule(final  Map<String, TrolleyItem> trolleyItems) {

        return trolleyItems
                .keySet()
                .stream()
                .map(key -> {
                    TrolleyItem trolleyItem = trolleyItems.get(key);
                    BigDecimal discountedAmount = new BigDecimal(0);
                    if (trolleyItem != null){
                        BigDecimal fullPrice = trolleyItem.getLineItem().getPrice().multiply(BigDecimal.valueOf(trolleyItem.getQuantity()));
                        discountedAmount = fullPrice.multiply(ZERO_POINT_TWO);
                    }

                    return discountedAmount.setScale(2, BigDecimal.ROUND_CEILING);
                })
                .reduce((prev, current) -> prev.add(current))
                .orElse(new BigDecimal(0).setScale(2, BigDecimal.ROUND_CEILING));

    }
}
