package ggc;

public class ToStringDisplay extends TransactionDisplay{
    @Override
    public String showAcquisition(Acquisition a) {
        return a.toString();
    }

    @Override
    public String showSale(Sale s) {
        return s.toString();
    }

    @Override
    public String showBreakdown(Breakdown b) {
        return b.toString();
    }
}
