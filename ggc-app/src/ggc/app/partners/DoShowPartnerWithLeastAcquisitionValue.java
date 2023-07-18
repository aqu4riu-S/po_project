package ggc.app.partners;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


public class DoShowPartnerWithLeastAcquisitionValue extends Command<WarehouseManager>{
    DoShowPartnerWithLeastAcquisitionValue(WarehouseManager receiver) {
        super(Label.SHOW_PARTNER_WITH_LEAST_ACQUISITIONS_VALUE, receiver);
    }

    @Override
    public final void execute() {
        _display.addLine(_receiver.getPartnerWithLeastAcquisitionValue());
        _display.display();
    }
}
