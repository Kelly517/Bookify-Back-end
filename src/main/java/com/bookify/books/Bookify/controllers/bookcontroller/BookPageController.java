package com.bookify.books.Bookify.controllers.bookcontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.bookpaths.BookPagePaths;
import com.bookify.books.Bookify.model.dto.bookdto.BookPageDTO;
import com.bookify.books.Bookify.model.entities.bookentities.BookPageEntity;
import com.bookify.books.Bookify.shared.interfaces.bookinterfaces.BookPageInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class BookPageController {

    private final BookPageInterface bookPageService;

    @PostMapping(BookPagePaths.SAVE_URL)
    public ResponseEntity<BookPageDTO> saveBookPage(@PathVariable String bookIdentifierCode, @RequestBody BookPageDTO bookPageDTO) {
        BookPageDTO bookPage = bookPageService.createBookPage(bookPageDTO, bookIdentifierCode);
        return new ResponseEntity<>(bookPage, HttpStatus.CREATED);
    }

    @PutMapping(BookPagePaths.EDIT_URL)
    public ResponseEntity<BookPageDTO> updateBookPage(@PathVariable Long bookPageId, @RequestBody BookPageDTO bookPageDTO) throws IOException {
        BookPageDTO bookPage = bookPageService.updateBookPage(bookPageId, bookPageDTO);
        return new ResponseEntity<>(bookPage, HttpStatus.OK);
    }

    @GetMapping("pages/{bookId}")
    public ResponseEntity<Set<BookPageEntity>> getAllPAges(@PathVariable Long bookId) {
        Set<BookPageEntity> allBooks = bookPageService.getAllPages(bookId);
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @GetMapping("page/{bookPageId}/{pageNumber}/{bookId}")
    public ResponseEntity<BookPageDTO> getBook( @PathVariable Long bookPageId, @PathVariable int pageNumber, @PathVariable Long bookId) {
        BookPageDTO page = bookPageService.getIndividualPage(bookPageId, pageNumber, bookId);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/content/page/{bookPageId}")
    public ResponseEntity<String> extractContentPage(@PathVariable Long bookPageId) {
        String pageContent = bookPageService.extractContentPage(bookPageId);
        return new ResponseEntity<>(pageContent, HttpStatus.OK);
    }
}
