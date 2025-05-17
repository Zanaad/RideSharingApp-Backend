package com.ridesharingappbackend.utils;

import com.ridesharingappbackend.service.EmailService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class EmailQueue {

    private final BlockingQueue<EmailJob> queue = new LinkedBlockingQueue<>();
    private final EmailService emailService;

    public EmailQueue(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostConstruct
    public void init() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                EmailJob job = queue.take();
                emailService.send(job.getTo(), job.getSubject(), job.getBody());
            }
        });
    }

    public void enqueue(EmailJob job) {
        queue.add(job);
    }
}
