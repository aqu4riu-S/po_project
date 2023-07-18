package ggc.app.main;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.io.IOException;
import ggc.exceptions.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /**
   * @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.save();
    } catch (MissingFileAssociationException mfae){
      try {
        String filename = Form.requestString(Prompt.newSaveAs());
        _receiver.saveAs(filename);
      } catch (IOException | MissingFileAssociationException e) {
        e.printStackTrace();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

}
