package com.bookify.books.Bookify.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageExtensionValidator implements ConstraintValidator<ValidImageExtension, String> {
    @Override
    public boolean isValid(String filePath, ConstraintValidatorContext context) {
        if (filePath == null || filePath.trim().isEmpty()) return true;
        return filePath.endsWith(".png") || filePath.endsWith(".jpg");
    }
}
