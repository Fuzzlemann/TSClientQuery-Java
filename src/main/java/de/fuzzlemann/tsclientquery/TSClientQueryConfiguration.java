package de.fuzzlemann.tsclientquery;

public class TSClientQueryConfiguration {

    private final String host;
    private final int port;
    private final String apiKey;

    public TSClientQueryConfiguration(String host, int port, String apiKey) {
        this.host = host;
        this.port = port;
        this.apiKey = apiKey;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getApiKey() {
        return apiKey;
    }
}
