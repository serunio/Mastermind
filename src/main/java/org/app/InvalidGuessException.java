package org.app;

public class InvalidGuessException extends RuntimeException {

    public final ErrorType type;

    public InvalidGuessException(ErrorType type) {
        super();
        this.type = type;
    }

}

enum ErrorType {
    BADLENGTH, BADSYMBOL

}


