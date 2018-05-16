package com.generic.retailer.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public final class DVD extends Product {

    private long runningTime;

    public DVD(){
        super();
    }

    @Builder
    private DVD(String sku,
                String name,
                BigDecimal price,
                String desc,
                long runningTime){
        super(sku, name, price, desc);
        this.runningTime = runningTime;
    }

    public static class DVDBuilder extends ProductBuilder {
        DVDBuilder (){
            super();
        }
    }
}
