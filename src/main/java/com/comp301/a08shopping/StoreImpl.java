package com.comp301.a08shopping;

import com.comp301.a08shopping.events.*;
import com.comp301.a08shopping.exceptions.OutOfStockException;
import com.comp301.a08shopping.exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class StoreImpl implements Store {
  private final String name;
  private final ArrayList<Product> products;
  private final ArrayList<StoreObserver> observers;

  public StoreImpl(String name) {
    if (name == null) throw new IllegalArgumentException("Name cannot be null");
    this.name = name;
    this.products = new ArrayList<Product>();
    this.observers = new ArrayList<StoreObserver>();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void addObserver(StoreObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }
    observers.add(observer);
  }

  @Override
  public void removeObserver(StoreObserver observer) {
    observers.remove(observer);
  }

  @Override
  public List<Product> getProducts() {
    return (List<Product>) this.products.clone();
  }

  @Override
  public Product createProduct(String name, double basePrice, int inventory) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (basePrice <= 0.00) {
      throw new IllegalArgumentException("Base price cannot be negative");
    }
    if (inventory < 0) {
      throw new IllegalArgumentException("Inventory cannot be negative");
    }

    ProductImpl prod = new ProductImpl(name, basePrice);
    prod.setCount(inventory);
    this.products.add(prod);
    prod.setStore(this);
    return prod;
  }

  public void notifyObservers(StoreEvent event) {
    for (StoreObserver observer : observers) {
      observer.update(event);
    }
  }

  @Override
  public ReceiptItem purchaseProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (!this.products.contains(product)) {
      throw new ProductNotFoundException("Product not found in store");
    }
    if (getIsInStock(product)) {
      notifyObservers(new PurchaseEvent(product, this));
      ((ProductImpl) product).subCount();
      if (((ProductImpl) product).getCount() == 0) {
        notifyObservers(new OutOfStockEvent(product, this));
      }
      if (getIsOnSale(product)) {
        return new ReceiptItemImpl(
            product.getName(), getSalePrice(product), ((ProductImpl) product).getStore().getName());
      } else {
        return new ReceiptItemImpl(
            product.getName(),
            product.getBasePrice(),
            ((ProductImpl) product).getStore().getName());
      }
    } else {
      throw new OutOfStockException("Product is out of stock");
    }
  }

  @Override
  public void restockProduct(Product product, int numItems) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (numItems < 0) {
      throw new IllegalArgumentException("Number of items cannot be negative");
    }
    if (!this.products.contains(product)) {
      throw new ProductNotFoundException("Product not found in store");
    }
    if (((ProductImpl) product).getCount() == 0) {
      notifyObservers(new BackInStockEvent(product, this));
    }
    ((ProductImpl) product).setCount(numItems);
  }

  @Override
  public void startSale(Product product, double percentOff) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (percentOff < 0.00 || percentOff > 1.00) {
      throw new IllegalArgumentException("Percent off must be between 0.00 and 1.00");
    }
    if (!this.products.contains(product))
      throw new ProductNotFoundException("Product not found in store");
    ((ProductImpl) product).setdiscountValue(percentOff);
    notifyObservers(new SaleStartEvent(product, this));
  }

  @Override
  public void endSale(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (!this.products.contains(product)) {
      throw new ProductNotFoundException("Product not found in store");
    }
    ((ProductImpl) product).setdiscountValue(0.00);
    notifyObservers(new SaleEndEvent(product, this));
  }

  @Override
  public int getProductInventory(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (!this.products.contains(product)) {
      throw new ProductNotFoundException("Product not found in store");
    }
    return ((ProductImpl) product).getCount();
  }

  @Override
  public boolean getIsInStock(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (!this.products.contains(product)) {
      throw new ProductNotFoundException("Product not found in store");
    }
    return ((ProductImpl) product).getCount() > 0;
  }

  @Override
  public double getSalePrice(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (!this.products.contains(product)) {
      throw new ProductNotFoundException("Product not found in store");
    }
    return product.getBasePrice() * (1 - ((ProductImpl) product).getdiscountValue());
  }

  @Override
  public boolean getIsOnSale(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null");
    }
    if (!this.products.contains(product))
      throw new ProductNotFoundException("Product not found in store");
    return ((ProductImpl) product).getdiscountValue() > 0.00;
  }
}
