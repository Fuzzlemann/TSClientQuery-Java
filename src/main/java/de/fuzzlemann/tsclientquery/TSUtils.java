package de.fuzzlemann.tsclientquery;

import de.fuzzlemann.tsclientquery.commands.ClientListCommand;
import de.fuzzlemann.tsclientquery.commands.WhoAmICommand;
import de.fuzzlemann.tsclientquery.objects.Client;

import java.util.List;

public class TSUtils {

    public static int getMyClientID() {
        return new WhoAmICommand().getResponse().getClientID();
    }

    public static int getMyChannelID() {
        return new WhoAmICommand().getResponse().getChannelID();
    }

    public static List<Client> getClients() {
        return new ClientListCommand().getResponse().getClientList();
    }
}
