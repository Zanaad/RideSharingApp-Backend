package com.ridesharingappbackend.utils;

import com.ridesharingappbackend.dto.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResponseWriterTest {

    @Test
    void toResponseEntity_ReturnsCorrectApiError() {
        String message = "Invalid input";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ResponseEntity<ApiError> response = ErrorResponseWriter.toResponseEntity(status, message);

        assertEquals(status, response.getStatusCode());

        ApiError body = response.getBody();
        assertNotNull(body);
        assertEquals(status.value(), body.getStatus());
        assertEquals(status.getReasonPhrase(), body.getError());
        assertEquals(message, body.getMessage());
        assertNotNull(body.getTimestamp());
    }
}
