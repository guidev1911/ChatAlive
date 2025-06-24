package com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions;

public class ConfirmationCodeExpiredException extends RuntimeException {
    public ConfirmationCodeExpiredException(String message) {
        super(message);
    }
}
