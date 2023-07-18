package ggc;

import java.io.Serializable;

public class Batch implements Comparable<Batch>, Serializable {

  private Product _product;
  private String _partnerID;
  private int _remainingUnits;
  private double _priceUnit;

  public Batch(Product product, String partnerID, int quantity, double priceUnit) {
    _product = product;
    _partnerID = partnerID;
    _remainingUnits = quantity;
    _priceUnit = priceUnit;
  }

  public void setProduct(Product product) {
    _product = product;
  }

  public Product getProduct() {
    return _product;
  }

  public String getPartnerID() {
    return _partnerID;
  }

  public double getPriceUnit() {
    return _priceUnit;
  }

  public void setPriceUnit(float priceUnit) {
    _priceUnit = priceUnit;
  }
  
  public void updateBatch(int quantity) {
    _remainingUnits += quantity;
  }

  public int getRemainingUnits() {
    return _remainingUnits;
  }

  public boolean isEmpty() {
    return _remainingUnits <= 0;
  }

  @Override
  public String toString() {
    return  _product.getName() + "|" + 
            _partnerID + "|" + 
            Math.round(_priceUnit) + "|" + 
            _remainingUnits;
  }

  @Override
  public int compareTo(Batch other) {
    /**
     * Compare products' natural order. If equal then
     * Compare parterns' ID natural order (String). If equal then
     * Compare remaining units' natural order (Integer).
     */

    int first = getProduct().compareTo(other.getProduct());
    if (first == 0) {
      int second = getPartnerID().compareTo(other.getPartnerID());
      if (second == 0) {
        int third = (int) Math.round(getPriceUnit() - other.getPriceUnit());
        if (third == 0) {
          return getRemainingUnits() - other.getRemainingUnits();
        } else {
          return third;
        }
      } else {
        return second;
      }
    } else {
      return first;
    }
  }
}