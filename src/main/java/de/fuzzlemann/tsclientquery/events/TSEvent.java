package de.fuzzlemann.tsclientquery.events;

import de.fuzzlemann.tsclientquery.TSParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public abstract class TSEvent {

    private final String input;
    protected Map<String, String> map;

    TSEvent(String input) {
        super();
        this.input = input;
        this.map = TSParser.parse(input);
    }

    public String getInput() {
        return input;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Name {
        String value();
    }
}
