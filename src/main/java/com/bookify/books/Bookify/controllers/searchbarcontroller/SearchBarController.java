package com.bookify.books.Bookify.controllers.searchbarcontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.searchbarpaths.SearchBarBookPath;
import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import com.bookify.books.Bookify.service.searchbarservice.SearchBarImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class SearchBarController {

    private final SearchBarImpl searchBar;

    @GetMapping(SearchBarBookPath.FIND_BOOK_PATH)
    public ResponseEntity<Page<BookCardDto>> findAllBooks(
            @RequestParam String query,
            @PageableDefault Pageable pageable) {
        try {
            Page<BookCardDto> book = searchBar.searchBook(query, pageable);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(book);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("An error occurred with the search bar");
        }
    }

    @GetMapping(SearchBarBookPath.FIND_USER_PATH)
    public ResponseEntity<Page<UserDTO>> findUserBySearchBar (
            @PathVariable String username,
            @PageableDefault Pageable pageable) {
        try {
            Page<UserDTO> userEntityDTOS = searchBar.searchBookByAuthor(username, pageable);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userEntityDTOS);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("An error occurred with the search bar");
        }
    }
}
