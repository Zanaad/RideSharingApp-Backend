package com.ridesharingappbackend.exceptions;

public class InvalidOrExpiredTokenException extends BaseException {
    public InvalidOrExpiredTokenException(String message) {
        super(message);
    }
}
