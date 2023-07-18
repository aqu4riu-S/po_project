package ggc;

public class Bargain implements Notification {

  private Product _product;

  public Bargain(Product product) {
    _product = product;
  }

  public String getProductID() {
    return _product.getName();
  }

  public String getType() {
    return "BARGAIN";
  }

  @Override
  public String toString () {
    return getType() + "|" + getProductID() + "|" + Math.round(_product.getLowestPrice());
  }
}
