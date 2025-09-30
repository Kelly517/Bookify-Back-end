package com.bookify.books.Bookify.components.paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadImagesPaths {

    @Value("${bookify.upload-dir}")
    public static String uploadDir;
    public static final String API_PATH = "api/cover_page/view/";
}
