package de.fuzzlemann.tsclientquery.commands;

import de.fuzzlemann.tsclientquery.CommandResponse;

public class AuthCommand extends BaseCommand<CommandResponse> {
    public AuthCommand(String apiKey) {
        super("auth apikey=" + apiKey);
    }
}
