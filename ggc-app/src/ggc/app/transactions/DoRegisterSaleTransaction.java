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
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partnerID", Prompt.partnerKey());
    addIntegerField("deadline", Prompt.paymentDeadline());
    addStringField("productID", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.registerSaleTransaction(
          stringField("partnerID"),
          integerField("deadline"),
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
