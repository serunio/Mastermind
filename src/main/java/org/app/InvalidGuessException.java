package org.app;

public class InvalidGuessException extends RuntimeException {

    public final char mode;

    public InvalidGuessException(char mode) {
        super();
        this.mode = mode;
    }
}
