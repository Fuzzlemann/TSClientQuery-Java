package de.fuzzlemann.tsclientquery.commands;

import de.fuzzlemann.tsclientquery.CommandResponse;
import de.fuzzlemann.tsclientquery.objects.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientListCommand extends BaseCommand<ClientListCommand.Response> {

    public ClientListCommand() {
        super("clientlist");
    }

    public static class Response extends CommandResponse {
        private final List<Client> clientList = new ArrayList<>();

        public Response(String rawResponse) {
            super(rawResponse);
            for (Map<String, String> map : getResponseList()) {
                clientList.add(new Client(map));
            }
        }

        public List<Client> getClientList() {
            return clientList;
        }
    }
}
