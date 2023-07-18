package ggc;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.ArrayList;

public class Breakdown extends Transaction{

    LinkedHashMap<Product, ArrayList<Integer>> _components = new LinkedHashMap<>();

    public Breakdown(int due, Partner partner, Product product, int quantity, double price) {
        super(due, partner, product, quantity, price);
    }

    public void setComponents(LinkedHashMap<Product, ArrayList<Integer>> components) {
        _components = components;
    }

    public LinkedHashMap<Product, ArrayList<Integer>> getComponents() {
        return _components;
    }

    @Override
    public String accept(ToStringDisplay visitor) {
        return visitor.showBreakdown(this);
    }

    @Override
    public String toString() {
        String s = "";
        for (Entry<Product, ArrayList<Integer>> e : _components.entrySet()) {
            s += e.getKey().getName() + ":" + e.getValue().get(0) + ":" + e.getValue().get(1) + "#";
        }
        s = s.substring(0, s.length() - 1);
        return "DESAGREGAÇÃO" + "|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getName() + "|"
                + getQuantity() + "|" + (int) Math.round(getPrice()) + "|" + (int) Math.round(getPriceToPay()) + "|" 
                + getDue() + "|" + s;
    }
}
