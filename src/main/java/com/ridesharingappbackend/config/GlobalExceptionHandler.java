package com.ridesharingappbackend.config;

import com.ridesharingappbackend.dto.ApiError;
import com.ridesharingappbackend.exceptions.*;
import com.ridesharingappbackend.utils.ErrorResponseWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError> handleCustomException(BaseException ex) {
        HttpStatus status = determineStatus(ex);
        return ErrorResponseWriter.toResponseEntity(status, ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(NoResourceFoundException ex) {
        return ErrorResponseWriter.toResponseEntity(HttpStatus.NOT_FOUND, "Resource not found.");
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleAllExceptions(Throwable ex) {
        String className = ex.getClass().getName();

        boolean isFrameworkIssue =
                ex instanceof NoSuchMethodError ||
                        className.startsWith("org.springdoc.") ||
                        className.contains("springdoc") ||
                        className.contains("ControllerAdviceBean") ||
                        className.startsWith("org.springframework.");

        if (isFrameworkIssue) {
            System.err.println("[IGNORED Framework Error] " + className + ": " + ex.getMessage());
            return ErrorResponseWriter.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error: " + ex.getMessage());
        }

        ex.printStackTrace();
        return ErrorResponseWriter.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidOrExpiredTokenException.class)
    public ResponseEntity<?> handleInvalidOrExpiredToken(InvalidOrExpiredTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


    private HttpStatus determineStatus(BaseException ex) {
        if (ex instanceof InvalidCredentialsException) return HttpStatus.UNAUTHORIZED;
        if (ex instanceof DuplicateUserException) return HttpStatus.CONFLICT;
        if (ex instanceof ResourceNotFoundException) return HttpStatus.NOT_FOUND;
        return HttpStatus.BAD_REQUEST;
    }
}
