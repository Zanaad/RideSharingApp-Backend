package com.ridesharingappbackend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailJob {
    private final String to;
    private final String subject;
    private final String body;
}
