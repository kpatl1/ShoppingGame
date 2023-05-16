package com.comp301.a08shopping.events;

import com.comp301.a08shopping.Product;
import com.comp301.a08shopping.Store;
import com.comp301.a08shopping.events.StoreEvent;

public class SaleEndEvent implements StoreEvent {
  private final Product product;
  private final Store store;

  public SaleEndEvent(Product product, Store store) {
    if (product == null || store == null)
      throw new IllegalArgumentException("Product or Store cannot be null");
    this.product = product;
    this.store = store;
  }

  @Override
  public Product getProduct() {
    return this.product;
  }

  @Override
  public Store getStore() {
    return this.store;
  }
}
