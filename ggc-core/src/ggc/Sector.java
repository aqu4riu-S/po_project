package ggc;
/*
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.io.Serializable;

public class Sector implements Comparable<Sector>, Serializable {

  private Product _product;
  private int _totalStock;
  private TreeSet<Batch> _batches = new TreeSet<>();

  public Sector(Product product, int stock, Batch firstBatch) {
    _product = product;
    _totalStock = stock;
    _batches.add(firstBatch);
  }

  public void setProduct(Product product) {
    _product = product;
  }

  public void addBatch(Batch batch) {
    _batches.add(batch);
    _totalStock += batch.getRemainingUnits();
  }

  public void addMultipleBatches(Collection<Batch> batches) {
    _batches.addAll(batches);
  }

  public void removeBatch(Batch batch) {
    _batches.remove(batch);
  }

  public Batch getHighestPriceBatch() {
    return _batches.last();
  }

  public void storeBatch(Batch batch) {
    _batches.add(batch);
  }

  public Product getProduct() {
    return _product;
  }

  public int getTotalStock() {
    return _totalStock;
  }

  public void setTotalStock(int n) {
    _totalStock = n;
  }

  public void updateTotalStock(int increment) {
    _totalStock += increment;
  }

  public TreeSet<Batch> getBatches() {
    return _batches;
  }

  @Override
  public int compareTo(Sector other) {
    return getProduct().compareTo(other.getProduct());
  }
}
*/