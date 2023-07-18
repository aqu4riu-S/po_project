package ggc.app.partners;

import ggc.WarehouseManager;
import ggc.app.exceptions.DuplicatePartnerKeyException;
import ggc.exceptions.PartnerAlreadyExistsException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

    DoRegisterPartner(WarehouseManager receiver) {
        super(Label.REGISTER_PARTNER, receiver);
        addStringField("id", Prompt.partnerKey());
        addStringField("name", Prompt.partnerName());
        addStringField("address", Prompt.partnerAddress());
    }

    @Override
    public void execute() throws CommandException {
        String id = stringField("id");
        String name = stringField("name");
        String address = stringField("address");
        try {
            _receiver.registerPartner(id, name, address);
        } catch (PartnerAlreadyExistsException paee) {
            throw new DuplicatePartnerKeyException(paee.getDuplicateKey());
        }
    }
}