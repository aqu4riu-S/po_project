package ggc;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.ArrayList;

public class Recipe implements Serializable {

  private LinkedHashMap<String, Integer> _inProducts = new LinkedHashMap<>();
  private double _productionCost = 0.0;


  public Recipe(LinkedHashMap<String, Integer> inProducts, double productionCost) {
    _inProducts = inProducts;
    _productionCost = productionCost;
  }

  public double getProductionCost() {
    return _productionCost;
  }

  public int getProportion(Product product) {
    return _inProducts.get(product.getName());
  }

  public LinkedHashMap<Product, Integer> getComponents() {
    LinkedHashMap<Product, Integer> components = new LinkedHashMap<>();
    for (String productID : _inProducts.keySet()) {
      components.put(new Product(productID), _inProducts.get(productID));
    }
    return components;
  }

  public LinkedHashMap<Product, ArrayList<Integer>> getComponentsProportional(int amount) {
    LinkedHashMap<Product, Integer> components = getComponents();
    LinkedHashMap<Product, ArrayList<Integer>> componentsProportional = new LinkedHashMap<>();
    for (Entry<Product, Integer> e : components.entrySet()) {
      ArrayList<Integer> value = new ArrayList<>(3); 
      /** index 0: Needed products
          index 1: Value of needed products
          index 2: Products available
        */
      value.add(0, amount * e.getValue());
      value.add(0);
      value.add(0);
      componentsProportional.put(e.getKey(), value);
    }
    return componentsProportional;
  }

  @Override
  public String toString() {
    String s = "";
    for (Entry<String, Integer> c : _inProducts.entrySet()) {
      s += c.getKey() + ":" + c.getValue() + "#";
    }
    return s.substring(0, s.length() - 1);
  }
}