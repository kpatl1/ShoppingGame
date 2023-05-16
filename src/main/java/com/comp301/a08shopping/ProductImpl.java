package com.comp301.a08shopping;

public class ProductImpl implements Product {

  private final String name;
  private final double basePrice;

  private int count;

  private double discountValue;

  private Store store;

  public ProductImpl(String name, double basePrice) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    this.name = name;
    if (basePrice < 0.00) {
      throw new IllegalArgumentException("Base price cannot be negative");
    }
    this.count = 0;
    this.discountValue = 0.00;
    this.basePrice = basePrice;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public int getCount() {
    return this.count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void subCount() {
    this.count--;
  }

  public double getdiscountValue() {
    return this.discountValue;
  }

  public void setdiscountValue(double discountValue) {
    this.discountValue = discountValue;
  }

  public Store getStore() {
    return this.store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  @Override
  public double getBasePrice() {
    return this.basePrice;
  }
}
