package com.ridesharingappbackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ridesharingappbackend.dto.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ErrorResponseWriter {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void write(HttpServletResponse res, HttpStatus status, String message) {
        try {
            ApiError error = new ApiError(
                    status.value(),
                    status.getReasonPhrase(),
                    message,
                    LocalDateTime.now()
            );

            res.setStatus(status.value());
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            PrintWriter writer = res.getWriter();
            writer.write(objectMapper.writeValueAsString(error));
            writer.flush();
            res.flushBuffer();
        } catch (IOException e) {
            System.err.println("Failed to write error response: " + e.getMessage());
        }
    }

    public static ResponseEntity<ApiError> toResponseEntity(HttpStatus status, String message) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error, headers, status);
    }
}
