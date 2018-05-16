package com.generic.retailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Product {

    public Product () {}
    private String sku;
    private String name;
    private BigDecimal price;
    private String desc;
}
