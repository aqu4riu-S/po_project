package ggc;

public abstract class TransactionDisplay {
    public abstract String showAcquisition(Acquisition a);
    public abstract String showSale(Sale s);
    public abstract String showBreakdown(Breakdown b);
}
