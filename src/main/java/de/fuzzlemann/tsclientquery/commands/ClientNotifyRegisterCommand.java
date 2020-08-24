package de.fuzzlemann.tsclientquery.commands;

import de.fuzzlemann.tsclientquery.CommandResponse;

public class ClientNotifyRegisterCommand extends BaseCommand<CommandResponse> {
    public ClientNotifyRegisterCommand(int schandlerID, String eventName) {
        super("clientnotifyregister schandlerid=" + schandlerID + " event=" + eventName);
    }
}
