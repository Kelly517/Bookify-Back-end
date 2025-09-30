package com.bookify.books.Bookify.constants;

import lombok.Getter;

@Getter
public enum BookStatus {

    PUBLIC("published"),
    PRIVATE("draft");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }
}
