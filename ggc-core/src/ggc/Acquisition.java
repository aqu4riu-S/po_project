package ggc;

public class Acquisition extends Transaction{


    public Acquisition(int due, Partner partner, Product product, int quantity, double price) {
        super(due, partner, product, quantity, price);
    }

    @Override
    public String accept(ToStringDisplay visitor) {
        return visitor.showAcquisition(this);
    }

    @Override
    public String toString() {
        return "COMPRA" + "|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getName() + "|"
                + getQuantity() + "|" + (int) Math.round(getPrice()) + "|" + getDue();
    }
}
