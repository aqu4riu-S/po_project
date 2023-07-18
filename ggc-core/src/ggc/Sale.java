package ggc;

public class Sale extends Transaction{

  public Sale(int due, int quantity) {
    super(due, quantity);
  }

  public Sale(int due, Partner partner, Product product, int quantity, double price) {
    super(due, partner, product, quantity, price);
  }

  @Override
  public String accept(ToStringDisplay visitor) {
    return visitor.showSale(this);
  }

  @Override
  public String toString() {
    String s = "";
    if (isPayed()) {
        s = "|" + getPaymentDate();
    }
    return "VENDA" + "|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getName() + "|"
      + getQuantity() + "|" + (int) Math.round(getPrice()) + "|" + (int) Math.round(getPriceToPay()) 
      + "|" + getDue() + s;
  }
}
