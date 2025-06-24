package com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions;

public class ConfirmationCodeAlreadySentException extends RuntimeException {
    public ConfirmationCodeAlreadySentException(String message) {
        super(message);
    }
}
