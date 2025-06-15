package com.guidev1911.ChatAlive.exception.customizedExceptions.groupExceptions;

public class GroupAlreadyExistsException extends RuntimeException {
    public GroupAlreadyExistsException(String message) {
        super(message);
    }
}