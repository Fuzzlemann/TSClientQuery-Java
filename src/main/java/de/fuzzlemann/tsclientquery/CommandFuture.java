package de.fuzzlemann.tsclientquery;

import de.fuzzlemann.tsclientquery.exceptions.ClientQueryFutureException;

import java.util.concurrent.CompletableFuture;

public class CommandFuture<T> extends CompletableFuture<T> {

    @Override
    public T get() {
        try {
            return super.get();
        } catch (Exception e) {
            throw new ClientQueryFutureException(e);
        }
    }

    @Override
    public boolean complete(Object commandResponse) {
        return super.complete((T) commandResponse);
    }
}
