package ggc.app.products;

import ggc.WarehouseManager;
import ggc.exceptions.ProductDoesNotExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Batch;
import ggc.app.exceptions.UnknownProductKeyException;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("id", Prompt.productKey());
  }

  @Override
  public final void execute() throws CommandException {
    String id = stringField("id");
    try {
      for (Batch b: _receiver.getBatchesWithProduct(id)) {
        _display.addLine(b);
      }
      _display.display();
    } catch (ProductDoesNotExistException pdnee) {
      throw new UnknownProductKeyException(pdnee.getUnknownKey());
    }
  }
}
