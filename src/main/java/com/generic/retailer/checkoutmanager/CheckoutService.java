package com.generic.retailer.checkoutmanager;

import com.generic.retailer.dto.Receipt;
import com.generic.retailer.trolley.Trolley;

public interface CheckoutService {

    void checkout(Trolley trolley);

    /**
     * Builds a reciept with the given trolley
     * @param trolley
     * @return
     */
    Receipt buildReciept(Trolley trolley);
    /**
     * Returns the receipt in a nice table
     * @param receipt
     * @return
     */
    String getReceiptAsString(final Receipt receipt);
}
