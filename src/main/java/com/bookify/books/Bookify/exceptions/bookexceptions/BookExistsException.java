package com.bookify.books.Bookify.exceptions.bookexceptions;

public class BookExistsException extends RuntimeException {
    public BookExistsException(String message) {
        super(message);
    }
}
