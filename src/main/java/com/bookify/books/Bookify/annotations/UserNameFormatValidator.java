package com.bookify.books.Bookify.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNameFormatValidator implements ConstraintValidator<ValidFormatUserName, String> {
    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{4,19}$";
    private final static Logger LOG = LoggerFactory.getLogger(UserNameFormatValidator.class.getName());

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext constraintValidatorContext) {
        if (userName != null && userName.matches(USERNAME_PATTERN)) {
            LOG.info("Valid user name input received.");
            return true;
        }

        if (userName == null || !userName.matches(USERNAME_PATTERN)) {
            LOG.info("""
                    User name cannot be null or contains an invalid format.\s
                    Please use this example to create your user name:\s
                    Bookify_user_549\s
                    or\s
                    Bookify549
                    """);
        }
        return false;
    }
}
