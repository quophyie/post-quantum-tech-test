package com.generic.retailer.checkoutmanager;

import com.generic.retailer.discountbroker.TrolleyItemsFilter;
import com.generic.retailer.discountbroker.DiscountBroker;
import com.generic.retailer.discountrules.DiscountRule;
import com.generic.retailer.dto.Receipt;
import com.generic.retailer.dto.ReceiptLine;
import com.generic.retailer.trolley.Trolley;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * Manages checking out
 */
public class CheckoutServiceImpl implements CheckoutService {

    private final DiscountBroker discountBroker;

    //Map specifying how filters map to discount rules
    private final Map<TrolleyItemsFilter, LinkedHashMap<String, DiscountRule>> trolleyItemsFiltersToDiscountRuleMap;

     public CheckoutServiceImpl(DiscountBroker discountBroker, Map<TrolleyItemsFilter, LinkedHashMap<String, DiscountRule>> trolleyItemsFiltersToDiscountRuleMap){

        this.trolleyItemsFiltersToDiscountRuleMap = trolleyItemsFiltersToDiscountRuleMap;

        requireNonNull(trolleyItemsFiltersToDiscountRuleMap, "trolleyItemsFiltersToDiscountRuleMap cannot be null");
        requireNonNull(discountBroker, "discountBroker cannot be null");
        this.discountBroker = discountBroker;
    }


    /**
     * Checkout of the shop
     * @param trolley
     */
    @Override
    public void checkout(Trolley trolley) {
      Receipt receipt = buildReciept(trolley);
      System.out.println(getReceiptAsString(receipt));
    }

    /**
     * Builds a reciept using trolley
     * @param trolley
     * @return
     */
    @Override
    public Receipt buildReciept(Trolley trolley) {

        requireNonNull(trolley, "trolley cannot be null");
        requireNonNull(trolley, "trolley.trolleyItems cannot be null");

        final List<ReceiptLine> receiptLines = aggregateTrolleyItems(trolley);
        final BigDecimal[] totalDiscountArr = {new BigDecimal(0)};

        final Set<TrolleyItemsFilter> trolleyItemsFilters = trolleyItemsFiltersToDiscountRuleMap.keySet();


        trolleyItemsFilters
                .forEach(trolleyItemsFilter -> {
                    // Get the rules that apply to the given filter
                    final LinkedHashMap<String, DiscountRule> discountRules = trolleyItemsFiltersToDiscountRuleMap.get(trolleyItemsFilter);
                    //apply the discount rules
                    discountRules.entrySet().forEach(entry -> {
                        BigDecimal discount = discountBroker.applyBrokerRule(trolleyItemsFilter, entry.getValue(), trolley.getTrolleyItems());

                        discount = discount == null ? BigDecimal.ZERO : discount;

                        if (discount.doubleValue() > 0) {
                            ReceiptLine receiptLine = ReceiptLine.builder()
                                    .amount(discount.negate())
                                    .receiptItemName(entry.getKey())
                                    .build();

                            totalDiscountArr[0] = totalDiscountArr[0].add(discount);

                            receiptLines.add(receiptLine);
                        }
                    });

                });

        final BigDecimal totalDiscount = totalDiscountArr[0];
        final BigDecimal total = calculateTotal(trolley, totalDiscount);

        Receipt receipt = Receipt.builder()
                .receiptLines(receiptLines)
                .total(total)
                .build();

        return receipt;
    }

    @Override
    /**
     * Retturns a printable reciept as a string
     */
    public String getReceiptAsString(Receipt receipt) {
        requireNonNull(receipt, "receipt cannot be null");
        requireNonNull(receipt.getReceiptLines(), "receipt.receiptLines cannot be null");

        final AsciiTable at = new AsciiTable();
        at.getContext().setGridTheme(TA_GridThemes.NONE);
        at.getContext().setWidth(22);
        at.getContext().setFrameTopMargin(0);
        at.getContext().setFrameBottomMargin(0);
        at.getContext().setFrameLeftMargin(0);

        at.addRow(  null,null,null,"===== RECEIPT ======");

        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "GB"));
        receipt.getReceiptLines()
                .forEach(receiptLine -> {
                    String lineAmt = numberFormat.format(receiptLine.getAmount());
                    AT_Row row1 = at.addRow(null,receiptLine.getReceiptItemName(),null, String.format("%s", lineAmt));
                    row1.getCells().get(1).getContext().setTextAlignment(TextAlignment.JUSTIFIED_LEFT);
                    row1.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
                });

        at.addRow( null,null,null, "====================").setTextAlignment(TextAlignment.CENTER);

        AT_Row totalRow = at.addRow( null,"TOTAL",null, numberFormat.format(receipt.getTotal()));
        totalRow.getCells().get(1).getContext().setTextAlignment(TextAlignment.LEFT);
        totalRow.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);


        String rendered = Pattern.compile("\n")
                .splitAsStream(at.render())
                .map(s -> s.trim())
                .collect(Collectors.joining("\n"));

        return rendered;
    }


    /**
     * aggregates the items in a trolley.
     * Note, this does not include the discount lines
     * @param trolley
     * @return
     */
    private List<ReceiptLine> aggregateTrolleyItems(Trolley trolley){

        return trolley.getTrolleyItems()
                .values()
                .stream()
                .map(trolleyItem -> {

                    BigDecimal amount = trolleyItem.getLineItem().getPrice().multiply(BigDecimal.valueOf(trolleyItem.getQuantity()));
                    String lineItemName = generateReceiptLine(trolleyItem.getLineItem().getName(), trolleyItem.getQuantity());

                    return ReceiptLine.builder()
                            .receiptItemName(lineItemName)
                            .amount(amount)
                            .build();
                })
                .collect(toList());
    }
    /**
     * Calculates the total of trolley items taking into account the discount
     * @param trolley
     * @param totalDiscount
     * @return
     */
    private BigDecimal calculateTotal(Trolley trolley, BigDecimal totalDiscount){

        return trolley.getTrolleyItems()
                .values()
                .stream()
                .map(trolleyItem -> trolleyItem.getLineItem().getPrice().multiply(BigDecimal.valueOf(trolleyItem.getQuantity())))
                .reduce((prev, current) -> prev.add(current))
                .orElse(BigDecimal.valueOf(0))
                .subtract(totalDiscount);
    }

    /**
     *
     * @param lineItemName
     * @param quantity
     * @return
     */
    private String generateReceiptLine(String lineItemName, int quantity){
        if (quantity> 1)
            return String.format("%s (x%d)", lineItemName, quantity);
        return String.format("%s", lineItemName);
    }
}
