package ggc.app.main;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show global balance.
 */
class DoShowGlobalBalance extends Command<WarehouseManager> {

  DoShowGlobalBalance(WarehouseManager receiver) {
    super(Label.SHOW_BALANCE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    _display.addLine(Message.currentBalance(_receiver.getAvailable(), _receiver.getAccounting()));
    _display.display();
  }
  
}
