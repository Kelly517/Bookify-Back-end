package com.bookify.books.Bookify.exceptions.bookexceptions;

public class BookAlreadyexistsException extends RuntimeException {
    public BookAlreadyexistsException(String message) {
        super(message);
    }
}
