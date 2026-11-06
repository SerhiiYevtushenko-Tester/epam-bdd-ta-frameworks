package com.epam.rd.autotasks.utils;

import com.epam.rd.autotasks.models.EmailMessage;
import com.epam.rd.autotasks.models.User;

public class TestDataFactory {

    private final ConfigProvider config;

    public TestDataFactory() {
        this.config = ConfigProvider.getInstance();
    }

    public User createUser() {
        String email = config.getRequiredProperty("mail.email");
        String password = config.getRequiredProperty("mail.password");

        return new User(email, password);
    }

    public String getRecipient() {
        String recipient = config.getProperty("test.recipient");

        if (recipient == null || recipient.trim().isEmpty()) {
            recipient = config.getRequiredProperty("mail.recipient");
        }

        return recipient;
    }

    public EmailMessage createEmailMessage() {
        String subject = config.getRequiredProperty("test.email.subject")
                + " " + System.currentTimeMillis();

        String body = config.getRequiredProperty("test.email.body");

        return EmailMessage.builder()
                .to(getRecipient())
                .subject(subject)
                .body(body)
                .build();
    }
}
