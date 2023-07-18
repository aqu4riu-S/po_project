package ggc;

import java.util.Comparator;

public class PriceComparator implements Comparator<Batch> {
  @Override
  public int compare(Batch b1, Batch b2) {
    return Double.compare(b1.getPriceUnit(), b2.getPriceUnit());
  }
}