package com.ridesharingappbackend.exceptions;

public class DuplicateUserException extends BaseException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
