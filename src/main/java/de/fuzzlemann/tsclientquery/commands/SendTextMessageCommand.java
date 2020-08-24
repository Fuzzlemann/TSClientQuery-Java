package de.fuzzlemann.tsclientquery.commands;

import de.fuzzlemann.tsclientquery.CommandResponse;
import de.fuzzlemann.tsclientquery.TSParser;
import de.fuzzlemann.tsclientquery.objects.Client;
import de.fuzzlemann.tsclientquery.objects.TargetMode;

public class SendTextMessageCommand extends BaseCommand<CommandResponse> {
    public SendTextMessageCommand(Client target, String message) {
        this(target.getClientID(), message);
    }

    public SendTextMessageCommand(int targetID, String message) {
        super("sendtextmessage targetmode=" + TargetMode.PRIVATE.getID() + " target=" + targetID + " msg=" + TSParser.encode(message));
    }

    public SendTextMessageCommand(TargetMode targetMode, String message) {
        super("sendtextmessage targetmode=" + targetMode.getID() + " msg=" + TSParser.encode(message));
    }
}
