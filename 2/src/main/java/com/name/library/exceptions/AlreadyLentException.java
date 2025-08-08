package com.name.library.exceptions;

public class AlreadyLentException extends RuntimeException {
    public AlreadyLentException(String message) {
        super(message);
    }
}
