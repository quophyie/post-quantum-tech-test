package com.generic.retailer.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public final class Book extends Product {


    private String synopsis;
    public Book(){
        super();
    }

    @Builder
    private Book(String sku,
                String name,
                BigDecimal price,
                String desc,
                String synopsis){
        super(sku, name, price, desc);
        this.synopsis = synopsis;
    }

    public static class BookBuilder extends ProductBuilder {
        BookBuilder (){
            super();
        }
    }

}
