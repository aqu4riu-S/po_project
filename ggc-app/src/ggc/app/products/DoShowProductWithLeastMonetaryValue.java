package ggc.app.products;

import ggc.WarehouseManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


public class DoShowProductWithLeastMonetaryValue extends Command<WarehouseManager>{

    DoShowProductWithLeastMonetaryValue(WarehouseManager receiver) {
        super(Label.SHOW_PRODUCT_WITH_LEAST_MONETARY_VALUE, receiver);
    }

    @Override
    public final void execute() throws CommandException {
        _display.addLine(_receiver.getProductWithLeastMonetaryValue());
        _display.display();
    }
}
