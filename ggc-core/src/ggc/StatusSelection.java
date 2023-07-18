package ggc;

class StatusSelection extends Status{

  public StatusSelection(int points, Partner partner) {
    super(points, partner);
  }



  @Override
  public void paymentRegister(Transaction transaction, int date) {
    if (transaction.calculatePeriod(date) <= 2) incrementPoints(transaction);
    else {
      decreasePoints(transaction, date);
      if (transaction.getDelay(date) > getSlack()) getPartner().setStatus(new StatusNormal(getPoints(), getPartner()));
    }
  }


  @Override
  public void breakdownRegister(Transaction transaction, int date) {
    if (transaction.calculatePeriod(date) <= 2) {
      incrementPoints(transaction);
      if (getPoints() > 2500) getPartner().setStatus(new StatusElite(getPoints(), getPartner()));
    }
  }


  @Override
  public float calculateFee(Transaction transaction, int date) {
    int period = transaction.calculatePeriod(date);
    int daysPast = date - transaction.getDue();
    switch (period) {
      case 3  : return (daysPast == 1) ? 1f : 1f + 0.02f*daysPast;
      case 4  : return 1f + 0.05f*daysPast;
      default : return 1f;
    }
  }

  @Override
  public float getPointsPenalization() {
    return 0.9f;
  }

  @Override
  public int getSlack() {
    return 2;
  }

  @Override
  public void punishStatus() {
    getPartner().setStatus(new StatusNormal(getPoints(), getPartner()));
  }

  @Override
  public String status() {
    return "SELECTION";
  }
}