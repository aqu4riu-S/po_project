package ggc;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

public class Partner extends NotifiableEntity implements Serializable, Comparable<Partner> {
  
  private String _id;
  private String _name;
  private String _address;
  private Status _status;
  private double _acquisitionsValue = 0;
  private double _salesValue = 0; // includes non-paid sales
  private double _paidSalesValue = 0;


  public Partner(String id) {
    _id = id;
  }

  public Partner(String id, String name, String address) {
    this(id);
    _name = name;
    _address = address;
    _status = new StatusNormal(0, this);
  }


  public void paymentRegister(Transaction t, int date) { _status.paymentRegister(t, date);}
  public void breakdownRegister(Transaction t, int date) { _status.breakdownRegister(t, date);}



  public String getId() {
    return _id;
  }

  public String getName() {
    return _name;
  }

  public String getAddress() {
    return _address;
  }

  public double getAcquisitionsValue() {
    return _acquisitionsValue;
  }

  public double getSalesValue() {
    return _salesValue;
  }

  public double getPaidSalesValue() {
    return _paidSalesValue;
  }

  public void updateAcquistionsValue(double increment) {
    _acquisitionsValue += increment;
  }

  public void updateSalesValue(double increment) {
    _salesValue += increment;
  }

  public void updatePaidSalesValue(double increment) {
    _paidSalesValue += increment;
  }

  protected void setStatus(Status status) {
    _status = status;
  }

  public String getStatus() {
    return _status.status();
  }

  public double getPriceToPay(Transaction transaction, int date) {
    return transaction.getPrice() 
      * _status.calculateDiscount(transaction, date)
      * _status.calculateFee(transaction, date);
  }

  @Override
  public String toString() {
    return getId() + "|" +
           getName() + "|" + 
           getAddress() + "|" + 
           _status.status() + "|" + 
           _status.getPoints() + "|" + 
           Math.round(getAcquisitionsValue()) + "|" + 
           Math.round(getSalesValue()) + "|" + 
           Math.round(getPaidSalesValue());
  }

  @Override
  public int compareTo(Partner other) {
    return _id.toUpperCase().compareTo(other.getId().toUpperCase());
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Partner) {
      Partner partner = (Partner) other;
      return partner.getId().toUpperCase().equals(this.getId().toUpperCase());
    } else {
      return false;
    }
  }
}