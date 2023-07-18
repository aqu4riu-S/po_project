package ggc.app.products;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Batch;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("id", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    String id = stringField("id");
    try {
      for (Batch b: _receiver.getBatchesWithPartner(id)) {
        _display.addLine(b);
      }
      _display.display();
    } catch (PartnerDoesNotExistException pdnee) {
      throw new UnknownPartnerKeyException(pdnee.getUnknownKey());
    }
  }

}
