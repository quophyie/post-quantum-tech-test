package com.generic.retailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ReceiptLine {

    private String receiptItemName;
    private BigDecimal amount;
}
