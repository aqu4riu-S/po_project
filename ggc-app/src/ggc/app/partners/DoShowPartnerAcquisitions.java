package ggc.app.partners;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.Acquisition;
import java.util.LinkedList;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    super.addStringField("partner", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerID = super.stringField("partner");
    try {
      LinkedList<Acquisition> acquisitions = _receiver.showPartnerAcquisitions(partnerID);
      if (acquisitions != null) {
        for (Acquisition a : acquisitions) {
          _display.addLine(a);
        }
        _display.display();
      }
    } catch (PartnerDoesNotExistException pdnee) {
      throw new UnknownPartnerKeyException(pdnee.getUnknownKey());
    }
  }
}
