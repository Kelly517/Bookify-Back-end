package com.bookify.books.Bookify.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFormatUserName {
    String message() default "Invalid user name format. It can only contain letters, numbers or underscore and be between 5 and 15 characters long.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
