package ggc.app.partners;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.Transaction;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.app.exceptions.UnknownPartnerKeyException;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("id", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String id = stringField("id");
    try {
      for (Transaction t : _receiver.showPartnerSalesAndBreakdowns(id)) {
        _display.addLine(t);
      }
      _display.display();
    } catch (PartnerDoesNotExistException e) {
      throw new UnknownPartnerKeyException(e.getUnknownKey());
    }
  }

}
