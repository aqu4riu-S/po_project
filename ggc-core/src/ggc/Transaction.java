package ggc;

import java.io.Serializable;

public class Transaction implements Serializable {

  private int _id;
  private int _due;
  private int _paymentDate;
  private Partner _partner;
  private Product _product;
  private int _quantity;
  private double _price;
  private double _priceToPay;
  private boolean _payed = false;


  public Transaction(int due, Partner partner, Product product, int quantity, double price) {
    this(due, quantity);
    _partner = partner;
    _product = product;
    _price = price;
  }

  public Transaction(int due, int quantity) {
    _due = due;
    _quantity = quantity;
  }

  // Setters
  public void setId(int id) {
    _id = id;
  }

  public void setDue(int due) {
    _due = due;
  }

  public void setPartner(Partner partner) {
    _partner = partner;
  }

  public void setProduct(Product product) {
    _product = product;
  }

  public void setQuantity(int quantity) {
    _quantity = quantity;
  }

  public void setPrice(double price) {
    _price = price;
  }

  public void setPriceToPay(double priceToPay) {
    _priceToPay = priceToPay;
  }

  public void paymentComplete(int date) {
    _payed = true;
    _paymentDate = date;
  }

  // Getters
  public int getId() {
    return _id;
  }

  public int getDue() {
    return _due;
  }

  public int getPaymentDate() {
    return _paymentDate;
  }

  public Partner getPartner() {
    return _partner;
  }

  public Product getProduct() {
    return _product;
  }

  public int getQuantity() {
    return _quantity;
  }

  public double getPrice() {
    return _price;
  }
  
  public boolean isPayed() {
    return _payed;
  }

  public double getPriceToPay() {
    return _priceToPay;
  }

  public double calculatePriceToPay(int date) {
    return getPartner().getPriceToPay(this, date);
  }

  public void updatePriceToPay(int date) {
    setPriceToPay(calculatePriceToPay(date));
  }

  public int getDelay(int date) {
    return date - getDue();
  }

  // Other Methods

  public String accept(ToStringDisplay visitor) {
    throw new UnsupportedOperationException();
  }

  public int calculatePeriod(int currentDate) {
    int dueDistance = _due - currentDate;
    int N = _product.getInterval();
    if (dueDistance >= N) { return 1; }
    else if (0 <= dueDistance && dueDistance < N) { return 2; }
    else if (0 > dueDistance && dueDistance > -N) { return 3; }
    else { return 4; } /** (-N > dueDistance) */
  }
}