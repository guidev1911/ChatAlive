package com.guidev1911.ChatAlive.exception.customizedExceptions.emailExceptions;

public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException(String message) {
        super(message);
    }
}
