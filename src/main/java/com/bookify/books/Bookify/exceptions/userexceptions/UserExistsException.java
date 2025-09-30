package com.bookify.books.Bookify.exceptions.userexceptions;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String message) {
        super(message);
    }
}
