package ggc.app.partners;

import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.exceptions.ProductDoesNotExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partnerID", Prompt.partnerKey());
    addStringField("productID", Prompt.productKey());

  }

  @Override
  public void execute() throws CommandException {
    try {
      String partnerID = stringField("partnerID");
      String productID = stringField("productID");
      _receiver.toggleProductNotifications(partnerID, productID);
    }
    catch (ProductDoesNotExistException productdnee) {
      throw new UnknownProductKeyException(productdnee.getUnknownKey());
    }
    catch (PartnerDoesNotExistException partnerdnee) {
      throw new UnknownPartnerKeyException(partnerdnee.getUnknownKey());
    }
  }
}
