package com.generic.retailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TrolleyItem {
    private Product lineItem;
    private int quantity;
}
