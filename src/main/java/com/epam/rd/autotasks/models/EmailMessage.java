package com.epam.rd.autotasks.models;

import java.util.Objects;

public class EmailMessage {
    private final String to;
    private final String subject;
    private final String body;

    private EmailMessage(Builder builder) {
        this.to = builder.to;
        this.subject = builder.subject;
        this.body = builder.body;
    }

    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String to;
        private String subject;
        private String body;

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public EmailMessage build() {
            Objects.requireNonNull(to, "Recipient field 'to' is required and cannot be null");
            if (to.trim().isEmpty()) {
                throw new IllegalArgumentException("Recipient field 'to' cannot be empty");
            }

            return new EmailMessage(this);
        }
    }
}