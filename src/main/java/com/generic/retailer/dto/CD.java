package com.generic.retailer.dto;

import lombok.Builder;

import java.math.BigDecimal;

public final class CD extends Product {

    private long size;

    public CD(){
        super();
    }

    @Builder
    private CD(String sku,
                 String name,
                 BigDecimal price,
                 String desc,
                 long size){
        super(sku, name, price, desc);
        this.size = size;
    }

    public static class CDBuilder extends ProductBuilder {
        CDBuilder (){
            super();
        }
    }
}
