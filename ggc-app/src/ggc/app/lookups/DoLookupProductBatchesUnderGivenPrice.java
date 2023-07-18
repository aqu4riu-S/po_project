package ggc.app.lookups;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Batch;

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addRealField("priceLimit", Prompt.priceLimit());
  }

  @Override
  public void execute() throws CommandException {
    double priceLimit = realField("priceLimit");
    for (Batch b : _receiver.getAllBatchesUnderPrice(priceLimit)) {
      _display.addLine(b);
    }
    _display.display();
  }

}
