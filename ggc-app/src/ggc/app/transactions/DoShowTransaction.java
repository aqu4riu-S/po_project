package ggc.app.transactions;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.exceptions.InvalidTransactionIDException;
import ggc.app.exceptions.UnknownTransactionKeyException;

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("id", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    int id = integerField("id");
    try {
      String t = _receiver.getTransaction(id);
      _display.addLine(t);
      _display.display();
    } catch (InvalidTransactionIDException e) {
      throw new UnknownTransactionKeyException(e.getUnknownKey());
    }
  }
}
