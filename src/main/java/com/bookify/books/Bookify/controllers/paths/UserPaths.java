package com.bookify.books.Bookify.controllers.paths;

public interface UserPaths {
    String POST_REGISTER = "/user";
    String GET_USER = "/users/{email}";
    String PUT_USER = "/user/{email}";
    String DELETE_USER = "/user/{email}";
}
