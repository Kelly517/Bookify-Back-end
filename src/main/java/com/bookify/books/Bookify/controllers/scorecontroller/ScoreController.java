package com.bookify.books.Bookify.controllers.scorecontroller;

import com.bookify.books.Bookify.model.dto.bookdto.BookRatingDto;
import com.bookify.books.Bookify.shared.interfaces.scoreinterface.BookRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookify")
@RequiredArgsConstructor
public class ScoreController {

    private final BookRatingService bookRatingService;

    @PostMapping("/score")
    public ResponseEntity<BookRatingDto> createRating(@RequestBody BookRatingDto bookRatingDto) {
        BookRatingDto createRate = bookRatingService.createRating(bookRatingDto);
        return new ResponseEntity<>(createRate, HttpStatus.CREATED);
    }
}
