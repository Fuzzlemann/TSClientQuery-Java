package de.fuzzlemann.tsclientquery.commands;

import de.fuzzlemann.tsclientquery.CommandResponse;

import java.util.Map;

public class WhoAmICommand extends BaseCommand<WhoAmICommand.Response> {

    public WhoAmICommand() {
        super("whoami");
    }

    public static class Response extends CommandResponse {

        private final int clientID;
        private final int channelID;

        public Response(String rawResponse) {
            super(rawResponse);
            Map<String, String> response = getResponse();
            this.clientID = parseInt(response.get("clid"));
            this.channelID = parseInt(response.get("cid"));
        }

        public int getClientID() {
            return clientID;
        }

        public int getChannelID() {
            return channelID;
        }
    }
}