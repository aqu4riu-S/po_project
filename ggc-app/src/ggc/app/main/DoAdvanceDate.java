package ggc.app.main;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exceptions.InvalidDateException;

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  private Integer _daysToAdvance = 0;

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("daysToAdvance", Prompt.daysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {
    Integer daysToAdvance = integerField("daysToAdvance");
    if (daysToAdvance > 0){
      _receiver.advanceDate(daysToAdvance);
    } else {
      throw new InvalidDateException(daysToAdvance);
    }
  }

}
