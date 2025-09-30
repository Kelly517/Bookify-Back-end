package com.bookify.books.Bookify.components;

import io.github.cdimascio.dotenv.Dotenv;

public interface SuperAdmin {

    Dotenv dotenv = Dotenv.load();

    String SUPER_ADMIN_NAME = dotenv.get("SUPER_ADMIN_NAME");
    String SUPER_ADMIN_EMAIL = dotenv.get("SUPER_ADMIN_EMAIL");
    String SUPER_ADMIN_PASSWORD = dotenv.get("SUPER_ADMIN_PASSWORD");
}