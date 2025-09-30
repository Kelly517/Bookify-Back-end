package com.bookify.books.Bookify.service.scoreservice;

import com.bookify.books.Bookify.model.dto.bookdto.BookRatingDto;
import com.bookify.books.Bookify.model.entities.bookentities.BookRating;
import com.bookify.books.Bookify.model.mappers.book.RatingMapper;
import com.bookify.books.Bookify.repository.bookrepositories.ScoreRepository;
import com.bookify.books.Bookify.shared.interfaces.scoreinterface.BookRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookRatingServiceImpl implements BookRatingService {

    private final ScoreRepository scoreRepository;
    private final RatingMapper ratingMapper;

    @Override
    public BookRatingDto createRating(BookRatingDto bookRatingDto) {
        BookRating rate = ratingMapper.dtoToEntity(bookRatingDto);
        scoreRepository.save(rate);
        return ratingMapper.entityToDto(rate);
    }
}
