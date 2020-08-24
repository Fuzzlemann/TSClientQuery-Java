package de.fuzzlemann.tsclientquery;

import com.google.common.util.concurrent.Uninterruptibles;
import de.fuzzlemann.tsclientquery.commands.WhoAmICommand;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

public class KeepAliveThread extends Thread implements Closeable {

    private final TSClientQuery clientQuery;
    private volatile boolean closed;

    public KeepAliveThread(TSClientQuery clientQuery) {
        this.clientQuery = clientQuery;

        setName("TSClientQuery-KeepAliveThread");
    }

    @Override
    public void run() {
        while (!closed) {
            if (!clientQuery.isAuthenticated()) continue;

            new WhoAmICommand().execute(clientQuery);

            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void close() {
        closed = true;
    }
}
