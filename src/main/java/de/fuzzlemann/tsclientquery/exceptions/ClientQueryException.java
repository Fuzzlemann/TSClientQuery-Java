package de.fuzzlemann.tsclientquery.exceptions;

public abstract class ClientQueryException extends RuntimeException {
    public ClientQueryException() {
        super();
    }

    public ClientQueryException(String message) {
        super(message);
    }

    public ClientQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientQueryException(Throwable cause) {
        super(cause);
    }
}
