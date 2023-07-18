package ggc;

class StatusElite extends Status{

  public StatusElite(int points, Partner partner) {
    super(points, partner);
  }
  
  @Override
  public float calculateDiscount(Transaction transaction, int date) {
    int period = transaction.calculatePeriod(date);
    switch (period) {
      case 1, 2   : return 0.9f;
      case 3      : return 0.95f;
      default     : return 1f;
    }
  }

  @Override
  public void paymentRegister(Transaction transaction, int date) {
    if (transaction.calculatePeriod(date) <= 2) incrementPoints(transaction);
    else {
      decreasePoints(transaction, date);
      if (transaction.getDelay(date) > getSlack())
        getPartner().setStatus(new StatusSelection(getPoints(), getPartner()));
    }
  }


  @Override
  public void breakdownRegister(Transaction transaction, int date) {
    if (transaction.calculatePeriod(date) <= 2) incrementPoints(transaction);
  }


  @Override
  public float calculateFee(Transaction transaction, int date) {
    return 1f;
  }

  @Override
  public float getPointsPenalization() {
    return 0.75f;
  }

  @Override
  public int getSlack() {
    return 15;
  }

  @Override
  public void punishStatus() {
    getPartner().setStatus(new StatusSelection(getPoints(), getPartner()));
  }

  @Override
  public String status() {
    return "ELITE";
  }
}