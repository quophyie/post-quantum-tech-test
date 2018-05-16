package com.generic.retailer.discountrules;

import com.generic.retailer.dto.TrolleyItem;

import java.math.BigDecimal;
import java.util.Map;

@FunctionalInterface
public interface DiscountRule {

    BigDecimal applyDiscountRule(final  Map<String, TrolleyItem> trolleyItems);
}
