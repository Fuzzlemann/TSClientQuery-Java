package de.fuzzlemann.tsclientquery;

import com.google.common.util.concurrent.Uninterruptibles;
import de.fuzzlemann.tsclientquery.commands.AuthCommand;
import de.fuzzlemann.tsclientquery.commands.BaseCommand;
import de.fuzzlemann.tsclientquery.commands.ClientNotifyRegisterCommand;
import de.fuzzlemann.tsclientquery.commands.CurrentSchandlerIDCommand;
import de.fuzzlemann.tsclientquery.exceptions.ClientQueryAuthenticationException;
import de.fuzzlemann.tsclientquery.exceptions.ClientQueryConnectionException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TSClientQuery implements Closeable {

    private static TSClientQuery instance;
    private static TSClientQueryConfiguration configuration;
    private final String apiKey;
    private Socket socket;
    private volatile ClientQueryWriter writer;
    private volatile ClientQueryReader reader;
    private volatile KeepAliveThread keepAliveThread;
    private volatile boolean authenticated;
    private volatile int schandlerID;

    private TSClientQuery(String apiKey) {
        this.apiKey = apiKey;
    }

    public static void setConfiguration(TSClientQueryConfiguration configuration) {
        TSClientQuery.configuration = configuration;
    }

    public static TSClientQuery getInstance() {
        if (instance == null) {
            instance = new TSClientQuery(configuration.getApiKey());
            try {
                instance.connect();
            } catch (IOException e) {
                throw new ClientQueryConnectionException("TeamSpeak ClientQuery failed setting up a connection", e);
            }
        }

        return instance;
    }

    public static void reconnect() {
        disconnect();
        getInstance();
    }

    public static void disconnect() {
        if (instance != null) {
            Logger.LOGGER.info("Closing the TeamSpeak Client Query Connection...");
            instance.close();
        }
    }

    public void executeCommand(BaseCommand<?> command) {
        Uninterruptibles.putUninterruptibly(writer.getQueue(), command);
    }

    private void connect() throws IOException {
        Logger.LOGGER.info("Setting up the TeamSpeak Client Query Connection...");

        setupConnection();
        authenticate();
        setupSchandlerID();
        registerEvents();
    }

    private void setupConnection() throws IOException {
        socket = new Socket(configuration.getHost(), configuration.getPort());

        socket.setTcpNoDelay(true);
        socket.setSoTimeout(4000);

        writer = new ClientQueryWriter(this, new PrintWriter(socket.getOutputStream(), true));
        reader = new ClientQueryReader(this, new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)));

        // welcome messages
        while (reader.getReader().ready()) {
            reader.getReader().readLine();
        }

        writer.start();
        reader.start();

        keepAliveThread = new KeepAliveThread(this);
        keepAliveThread.start();
    }

    private void authenticate() {
        if (apiKey.length() != 29)
            throw new ClientQueryAuthenticationException("API Key was not entered correctly (apiKey.length() != 29)");

        AuthCommand authCommand = new AuthCommand(apiKey);
        authCommand.execute(this);

        CommandResponse response = authCommand.getResponse();
        if (!response.succeeded()) throw new ClientQueryAuthenticationException("API Key was not entered correctly");

        authenticated = true;
    }

    private void setupSchandlerID() {
        CurrentSchandlerIDCommand.Response response = new CurrentSchandlerIDCommand().getResponse();
        this.schandlerID = response.getSchandlerID();
    }

    private void registerEvents() {
        int schandlerID = TSClientQuery.getInstance().getSchandlerID();
        for (String eventName : TSEventHandler.TEAMSPEAK_EVENTS.keySet()) {
            new ClientNotifyRegisterCommand(schandlerID, eventName).execute();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientQueryWriter getWriter() {
        return writer;
    }

    public ClientQueryReader getReader() {
        return reader;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public int getSchandlerID() {
        return schandlerID;
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(socket, writer, reader, keepAliveThread);
        TSClientQuery.instance = null;
    }
}
