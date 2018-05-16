package com.generic.retailer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Receipt {

    private List<ReceiptLine> receiptLines;
    private BigDecimal total;
}
