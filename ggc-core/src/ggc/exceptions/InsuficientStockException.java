package ggc.exceptions;

public class InsuficientStockException extends Exception {
  private String _productKey;
  private int _amount;
  private int _available;

  public InsuficientStockException(String productKey, int amount, int available) {
    _productKey = productKey;
    _amount = amount;
    _available = available;
  }

  public String getProductKey() {
    return _productKey;
  }

  public int getRequestedAmount() {
    return _amount;
  }

  public int getAvailableAmount() {
    return _available;
  }

}