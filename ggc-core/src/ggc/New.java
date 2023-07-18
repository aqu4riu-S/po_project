package ggc;

public class New implements Notification {

  private Product _product;

  public New(Product product) {
    _product = product;
  }

  public String getProductID() {
    return _product.getName();
  }
    
  public String getType() {
    return "NEW";
  }

  @Override
  public String toString() {
    return getType() + "|" + getProductID() + "|" + _product.getLowestPrice();
  }
}
