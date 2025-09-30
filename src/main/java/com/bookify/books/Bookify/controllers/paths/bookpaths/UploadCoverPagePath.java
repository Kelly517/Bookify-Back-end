package com.bookify.books.Bookify.controllers.paths.bookpaths;

public interface UploadCoverPagePath {
    String POST_URL = "/upload-image/{bookIdentifierCode}";
    String GET_URL = "/view-image/{title}/{file}";
}
