package ggc;

import java.io.Serializable;

abstract class Status implements Serializable {

  protected int _points;
  protected Partner _partner;

  public Status(int points, Partner partner) {
    _points = points;
    _partner = partner;
  }


  public abstract void paymentRegister(Transaction transaction, int date);
  public abstract void breakdownRegister(Transaction transaction, int date);

  public void setPoints(int points) {
    _points = points;
  }

  public int getPoints() {
    return _points;
  }

  public Partner getPartner() {
    return _partner;
  }
  
  public float calculateDiscount(Transaction transaction, int date) {
    int period = transaction.calculatePeriod(date);
    return (period == 1) ? 0.9f : 1f;
  }

  public abstract float calculateFee(Transaction transaction, int date);

  public void incrementPoints(Transaction transaction) {
    _points += transaction.getPrice()*10;
  }

  public abstract int getSlack();

  public abstract float getPointsPenalization();

  public void decreasePoints(Transaction transaction, int date) {
    if (transaction.getDelay(date) > getSlack())
      setPoints(Math.round(getPoints()*(1 - getPointsPenalization())));
  }

  public abstract void punishStatus();

  public abstract String status();

}