package com.generic.retailer.cli;

import com.generic.retailer.discountbroker.DayOfWeekTrolleyItemsFilter;
import com.generic.retailer.discountbroker.DiscountBroker;
import com.generic.retailer.discountbroker.DiscountBrokerImpl;
import com.generic.retailer.discountbroker.Product2For1TrolleyItemsFilter;
import com.generic.retailer.discountbroker.TrolleyItemsFilter;
import com.generic.retailer.discountrules.DiscountRule;
import com.generic.retailer.discountrules.TwentyPercentOffDiscountRule;
import com.generic.retailer.discountrules.TwoForOneDiscountRule;
import com.generic.retailer.dto.DVD;
import com.generic.retailer.dto.Product;
import com.generic.retailer.dto.Receipt;
import com.generic.retailer.inventory.InventoryService;
import com.generic.retailer.inventory.InventoryServiceImpl;
import com.generic.retailer.checkoutmanager.CheckoutService;
import com.generic.retailer.checkoutmanager.CheckoutServiceImpl;
import com.generic.retailer.inventory.InventoryRepository;
import com.generic.retailer.inventory.InventoryRepositoryImpl;
import com.generic.retailer.trolley.Trolley;
import com.generic.retailer.trolley.TrolleyImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public final class Cli implements AutoCloseable {

  public static Cli create(String prompt, BufferedReader reader, BufferedWriter writer, LocalDate date) {
    requireNonNull(prompt);
    requireNonNull(reader);
    requireNonNull(writer);
    return new Cli(prompt, reader, writer, date);
  }

  public static Cli create(BufferedReader reader, BufferedWriter writer) {
    return new Cli(">", reader, writer, LocalDate.now());
  }

  private static final Predicate<String> WHITESPACE = Pattern.compile("^\\s{0,}$").asPredicate();

  private final String prompt;
  private final BufferedReader reader;
  private final BufferedWriter writer;
  private final LocalDate date;
  private final CheckoutService checkoutService;
  private final InventoryService inventoryService;
  private final InventoryRepository inventoryRepository;
  private final Trolley trolley;

  private Cli(String prompt, BufferedReader reader, BufferedWriter writer, LocalDate date) {
    this.prompt = prompt;
    this.reader = reader;
    this.writer = writer;
    this.date = date;

    trolley = new TrolleyImpl();

    inventoryRepository  = new InventoryRepositoryImpl();
    inventoryService = new InventoryServiceImpl(inventoryRepository);

    DiscountBroker discountBroker = new DiscountBrokerImpl();


    final Map<TrolleyItemsFilter, LinkedHashMap<String, DiscountRule>> trolleyItemsFiltersToDiscountRuleMap = new HashMap<>();
    LinkedHashMap twoForDiscountRuleMap = new LinkedHashMap();
    twoForDiscountRuleMap.put("2 FOR 1", new TwoForOneDiscountRule());

    LinkedHashMap thursdayDiscountRuleMap = new LinkedHashMap();
    thursdayDiscountRuleMap.put("THURS", new TwentyPercentOffDiscountRule());

    trolleyItemsFiltersToDiscountRuleMap.put(new Product2For1TrolleyItemsFilter<>(DVD.class), twoForDiscountRuleMap);
    trolleyItemsFiltersToDiscountRuleMap.put(new DayOfWeekTrolleyItemsFilter(DayOfWeek.THURSDAY), thursdayDiscountRuleMap);

    checkoutService = new CheckoutServiceImpl(discountBroker,
            trolleyItemsFiltersToDiscountRuleMap);
  }



  private void prompt() throws IOException {
    writeLine(prompt);
  }

  private Optional<String> readLine() throws IOException {
    String line = reader.readLine();
    return line == null || WHITESPACE.test(line) ? Optional.empty() : Optional.of(line);
  }

  private void writeLine(String line) throws IOException {
    writer.write(line);
    writer.newLine();
    writer.flush();
  }

  public void run() throws IOException {
    writeLine("What would you like to buy?");
    prompt();
    Optional<String> line = readLine();
    while (line.isPresent()) {
      addProductToTrolley(line.get());
      writeLine("Would you like anything else?");
      prompt();
      line = readLine();
    }
    Receipt receipt = checkoutService.buildReciept(trolley);
    writeLine(String.format("Thank you for visiting Generic Retailer, your total is %s", receipt.getTotal()));
    writeLine(checkoutService.getReceiptAsString(receipt));
  }

  @Override
  public void close() throws Exception {
      reader.close();
      writer.close();
  }

  private void addProductToTrolley(String line){
    String[] prodNames = line.split(" ");
    if (prodNames != null) {
      Arrays.stream(prodNames)
              .forEach(productName -> {
                try {
                  if (inventoryService.isInInventory(productName)) {

                    Product product = inventoryService.getProduct(productName).get();
                    trolley.add(product);

                  } else {
                    writeLine(String.format("oops! thats embarassing. we do not have %s in stock", productName));
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

    }
  }

}
