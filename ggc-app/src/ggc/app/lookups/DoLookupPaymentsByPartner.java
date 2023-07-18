package ggc.app.lookups;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.Transaction;

/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    super(Label.PAID_BY_PARTNER, receiver);
    addStringField("id", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try {
      for (Transaction t : _receiver.getPaymentsByPartner(stringField("id"))) {
        _display.addLine(t);
      }
      _display.display();
    } catch (PartnerDoesNotExistException pdnee) {
      throw new UnknownPartnerKeyException(pdnee.getUnknownKey());
    }
  }
}
