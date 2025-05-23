package com.ridesharingappbackend.utils;

import com.ridesharingappbackend.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailQueueTest {

    @Mock
    private EmailService emailService;

    private EmailQueue emailQueue;

    @BeforeEach
    void setUp() {
        emailQueue = new EmailQueue(emailService);
        emailQueue.init(); // starts background thread
    }

    @Test
    void testEnqueueAndProcessEmailJob() throws InterruptedException {
        EmailJob job = new EmailJob("test@example.com", "Subject", "Body");

        emailQueue.enqueue(job);

        // Wait briefly to allow background thread to process job
        Thread.sleep(100);

        verify(emailService, times(1)).send("test@example.com", "Subject", "Body");
    }
}
