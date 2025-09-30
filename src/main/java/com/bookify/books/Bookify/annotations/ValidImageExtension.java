package com.bookify.books.Bookify.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageExtensionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageExtension {
    String message() default "Invalid image format (must be .png or .jpg)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
