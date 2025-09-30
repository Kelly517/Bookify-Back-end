package com.bookify.books.Bookify.configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getJwtSecret() {
        return dotenv.get("BOOKIFY_JWT_SECRET");
    }
}
