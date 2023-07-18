package ggc.app.partners;

import ggc.Notification;
import ggc.Partner;
import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.exceptions.PartnerDoesNotExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("id", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String id = stringField("id");
    try {
      Partner p = _receiver.showPartner(id);
      _display.addLine(p);
      for (Notification n : p.getNotifications()) {
        _display.addLine(n);
      }
      // empties partner notifications list
      p.emptyNotificationsList();
      _display.display();
    } catch (PartnerDoesNotExistException pdnee) {
      throw new UnknownPartnerKeyException(pdnee.getUnknownKey());
    }
  }
}
