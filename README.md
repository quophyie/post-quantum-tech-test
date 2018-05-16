# Generic Retailer

Generic retailer is creating a self service CLI to allow developers to order products.

A developer started implementing but didn't get very far at all, the only thing left are these notes.

Prices:
 - Book £5
 - CD  £10
 - DVD £15
 
Todo's:

 - [ ] Calculate total
 - [ ] Print receipt (show items, price and total)
   - [ ] Aggregate items in receipt
   - [ ] items left aligned, prices right aligned
   - [ ] see CliTest fo receipt format
 - [ ] Add discounts (2 for 1 on DVDs and 20% off on Thursdays)
   - [ ] Only one discount can apply to an item but customer should get best available discount per item. i.e. 3 DVDs on a Thursday would be £27 (2 for 1 and 20% off the 3rd).

## The Task

Given the above please finish implementing the rest of the system. 
Whilst this store has very few items at the moment, we know it's going to grow massively in the next few months.
Please create a solution that is extendable and makes use of common patterns and OO concepts where appropriate.
Please feel free to make use any third-party libraries.
(This solution should not take more that a few hours)
 
