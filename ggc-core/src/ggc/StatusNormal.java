package ggc;

class StatusNormal extends Status{

  public StatusNormal(int points, Partner partner) {
    super(points, partner);
  }


  @Override
  public void paymentRegister(Transaction transaction, int date) {
    if (transaction.calculatePeriod(date) <= 2) {
      incrementPoints(transaction);
      if (getPoints() > 2000) getPartner().setStatus(new StatusSelection(getPoints(), getPartner()));
    }
    else decreasePoints(transaction, date);
  }


  @Override
  public void breakdownRegister(Transaction transaction, int date) {
    if (transaction.calculatePeriod(date) <= 2) {
      incrementPoints(transaction);
      if (getPoints() > 2000 && getPoints() <= 2500) getPartner().setStatus(new StatusSelection(getPoints(), getPartner()));
      else if (getPoints() > 2500) getPartner().setStatus(new StatusElite(getPoints(), getPartner()));;
    }
  }


  @Override
  public float calculateFee(Transaction transaction, int date) {
    int period = transaction.calculatePeriod(date);
    int daysPast = date - transaction.getDue();
    switch (period){
      case 3  : return 1f + 0.05f*daysPast;
      case 4  : return 1f + 0.10f*daysPast;
      default : return 1f;
    }
  }

  @Override
  public float getPointsPenalization() {
    return 1f;
  }

  @Override
  public int getSlack() {
    return 0;
  }

  @Override
  public void punishStatus() {
    /** Stays in the same Status*/
  }

  @Override
  public String status() {
    return "NORMAL";
  }
}