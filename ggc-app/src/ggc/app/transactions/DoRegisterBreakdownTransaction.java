package ggc.app.transactions;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.exceptions.InsuficientStockException;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.exceptions.ProductDoesNotExistException;
import ggc.app.exceptions.UnavailableProductException;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("partnerID", Prompt.partnerKey());
    addStringField("productID", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.registerBreakdownTransaction(
          stringField("partnerID"), 
          stringField("productID"), 
          integerField("amount"));
    } catch (InsuficientStockException ise) {
      throw new UnavailableProductException(
        ise.getProductKey(),
        ise.getRequestedAmount(),
        ise.getAvailableAmount());
    } catch (PartnerDoesNotExistException partnerDNEE) {
      throw new UnknownPartnerKeyException(partnerDNEE.getUnknownKey());
    } catch (ProductDoesNotExistException productDNEE) {
      throw new UnknownProductKeyException(productDNEE.getUnknownKey());
    }
  }

}
