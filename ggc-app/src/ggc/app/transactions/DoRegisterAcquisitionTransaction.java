package ggc.app.transactions;

import ggc.WarehouseManager;
import ggc.app.exceptions.UnknownPartnerKeyException;
import ggc.app.exceptions.UnknownProductKeyException;
import ggc.exceptions.PartnerDoesNotExistException;
import ggc.exceptions.ProductDoesNotExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
import java.util.LinkedHashMap;


/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partnerID", Prompt.partnerKey());
    addStringField("productID", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {

    String partnerID = stringField("partnerID");
    String productID = stringField("productID");
    Double price = realField("price");
    int amount = integerField("amount");

    try {
      _receiver.verifyNotificationConditions(productID, price);
      _receiver.registerAcquisition(partnerID, productID, price, amount);
    }

    catch (PartnerDoesNotExistException partnerdnee) { throw new UnknownPartnerKeyException(partnerdnee.getUnknownKey()); }

    catch (ProductDoesNotExistException productdnee) {
      _receiver.turnProductNotifiable(productID);
      String addRecipe = Form.requestString(Prompt.addRecipe());
      if (!addRecipe.equals("n")) {
        // derived product
        LinkedHashMap<String, Integer> _inProducts = new LinkedHashMap<>();
        // ask for noComponents and alpha value
        addIntegerField("noOfComponents", Prompt.numberOfComponents());
        addRealField("alpha", Prompt.alpha());
        // loop to get recipe components
        for (int i = 0; i < integerField("noOfComponents"); i++) {
          // ask for component-quantity
          addStringField("productID", Prompt.productKey());
          addIntegerField("amount", Prompt.amount());
          _inProducts.put(stringField("productID"), integerField("amount"));
        }
        _receiver.addDerivedProduct(productID, price, _inProducts, realField("alpha"));
      }
      else _receiver.addProduct(productID, price);  //simple product

      try {_receiver.registerAcquisition(partnerID, productID, price, amount); }
      catch (PartnerDoesNotExistException partner2dnee) {throw new UnknownPartnerKeyException(partner2dnee.getUnknownKey());}
      catch (ProductDoesNotExistException product2dnee) {throw new UnknownProductKeyException(product2dnee.getUnknownKey());}
    }
  }
}
