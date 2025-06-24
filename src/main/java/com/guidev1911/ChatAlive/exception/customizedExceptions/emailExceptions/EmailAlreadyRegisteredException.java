package com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
