package de.fuzzlemann.tsclientquery.commands;

import de.fuzzlemann.tsclientquery.CommandResponse;
import de.fuzzlemann.tsclientquery.objects.Channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ChannelListCommand extends BaseCommand<ChannelListCommand.Response> {

    public ChannelListCommand() {
        super("channellist");
    }

    public static class Response extends CommandResponse {
        private final List<Channel> channels = new ArrayList<>();

        public Response(String rawResponse) {
            super(rawResponse);

            List<Map<String, String>> maps = getResponseList();

            for (Map<String, String> channelMap : maps) {
                channels.add(new Channel(channelMap));
            }

            Collections.sort(channels);
        }

        public List<Channel> getChannels() {
            return channels;
        }
    }
}
