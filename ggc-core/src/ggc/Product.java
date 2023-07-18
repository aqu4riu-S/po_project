package ggc;

import java.io.Serializable;
import ggc.exceptions.NoRecipeException;

public class Product implements Comparable<Product>, Serializable {
  
  private String _name = "";
  private double _maxPrice;
  private double _lowestPrice;
  private int _totalStock = 0;
  private int _interval;

  public Product(String name) {
    _name = name;
  }

  public Product(String name, double price) {
    _name = name;
    _maxPrice = price;
    _lowestPrice = price;
  }

  public Product(String name, double priceByUnit, int totalStock) {
    this(name);
    _maxPrice = priceByUnit;
    _lowestPrice = priceByUnit;
    _totalStock = totalStock;
    _interval = 5;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }


  public double getLowestPrice() {
    return _lowestPrice; 
  }

  public void setLowestPrice(double price) {
    _lowestPrice = price;
  }

  public double getMaxPrice() {
    return _maxPrice;
  }

  public void setMaxPrice(double maxPrice) {
    _maxPrice = maxPrice;
  }

  public int getTotalStock() {
    return _totalStock;
  }

  public void setTotalStock(int totalStock) {
    _totalStock = totalStock;
  }

  public void updateTotalStock(int increment) {
    _totalStock += increment;
  }

  public void setInterval(int interval) {
    _interval = interval;
  }

  public int getInterval() {
    return _interval;
  }

  public Recipe getRecipe() throws NoRecipeException {
    throw new NoRecipeException();
  }

  @Override
  public String toString() {
    return _name + "|" + Math.round(_maxPrice) + "|" + _totalStock;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Product) {
      Product p = (Product) other;
      return this.getName().equals(p.getName());
    }
    return false;
  }

  @Override
  public int compareTo(Product other) {
    return _name.toUpperCase().compareTo(other.getName().toUpperCase());
  }
}