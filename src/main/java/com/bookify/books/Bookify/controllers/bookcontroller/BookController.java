package com.bookify.books.Bookify.controllers.bookcontroller;

import com.bookify.books.Bookify.controllers.paths.ApiPaths;
import com.bookify.books.Bookify.controllers.paths.bookpaths.BookPaths;
import com.bookify.books.Bookify.shared.interfaces.bookinterfaces.BookInterface;
import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.bookdto.DtoCrearLibro;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(ApiPaths.API_URL + ApiPaths.BOOKIFY_API_ID)
@RequiredArgsConstructor
public class BookController {

    private final BookInterface bookService;
    private final static Logger LOG = LoggerFactory.getLogger(BookController.class.getName());

    @PostMapping(BookPaths.POST_URL)
    public ResponseEntity<DtoLibro> createNewBook(@Valid @RequestBody DtoCrearLibro dtoCrearLibro) {
        DtoLibro nuevoLibro = bookService.crearLibro(dtoCrearLibro);
        LOG.info("Book created: {}", nuevoLibro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    @GetMapping("/book/{bookIdentifierCode}")
    public ResponseEntity<DtoLibro> readBook(@PathVariable String bookIdentifierCode) {
        DtoLibro getBook = bookService.readBook(bookIdentifierCode);
        return new ResponseEntity<>(getBook, HttpStatus.OK);
    }

    @GetMapping("/books/purchased/{userId}")
    public ResponseEntity<Page<BookCardDto>> getAllPurchasedBooks(@PathVariable Long userId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<BookCardDto> purchasedBooks = bookService.getPurchasedBooks(userId, PageRequest.of(page, size));
        return new ResponseEntity<>(purchasedBooks, HttpStatus.OK);
    }

    @GetMapping("/books")
    public ResponseEntity<Page<BookCardDto>> readAllBooks(Pageable pageable) {
        Page<BookCardDto> getAllBooks = bookService.getAllBooks(pageable);
        return new ResponseEntity<>(getAllBooks, HttpStatus.OK);
    }

    @GetMapping("/books/rated")
    public ResponseEntity<List<BookCardDto>> getRatedBooks() {
        return new ResponseEntity<>(bookService.getTopRatedBooksOfWeek(), HttpStatus.OK);
    }

    @GetMapping("/books/category")
    public ResponseEntity<Page<BookCardDto>> getFilteredBooks(@RequestParam String category, Pageable pageable) {
        return new ResponseEntity<>(bookService.getBooksByCategory(category, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/book/{bookIdentifierCode}")
    public ResponseEntity<String> deleteBookByIsbn(@PathVariable String bookIdentifierCode) {
        String deleteBook = bookService.deleteBook(bookIdentifierCode);
        return new ResponseEntity<>(deleteBook, HttpStatus.OK);
    }
}

