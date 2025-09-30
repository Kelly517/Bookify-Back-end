package com.bookify.books.Bookify.email.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationEmailService {

    private final EmailSenderService emailSenderService;
    private final static Logger LOG = LoggerFactory.getLogger(VerificationEmailService.class.getName());

    public void sendVerificationEmail(String email, String code) {
        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            LOG.warn("Email and code cannot be empty. Please review info and try again");
            throw new IllegalArgumentException("Email and code cannot be empty. Please review info and try again");
        }

        emailSenderService.setJavaMailSender(email, code);
        LOG.info("Email sent to: {}", email);
    }

    public void setUpdateEmail(String email, String code) {
        if (email == null || email.isBlank() || code == null || code.isBlank()) {
            LOG.warn("Email and code cannot be empty. Please review info and try again ");
            throw new IllegalArgumentException("Email and code cannot be empty. Please review info and try again");
        }

        emailSenderService.setJavaMailUpdateSender(email, code);
        LOG.info("Email sent to : {}", email);
    }
}
