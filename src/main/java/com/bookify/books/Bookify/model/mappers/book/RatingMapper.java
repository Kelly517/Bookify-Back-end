package com.bookify.books.Bookify.model.mappers.book;

import com.bookify.books.Bookify.model.dto.bookdto.BookRatingDto;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.entities.bookentities.BookRating;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Builder
public class RatingMapper {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    // Convierte un BookRatingDto en una entidad BookRating (para guardar en la BD)
    public BookRating dtoToEntity(BookRatingDto bookRatingDto) {

        //validar
        // busca el libro en la BD usando el id que viene en el dto
        BookEntity book = bookRepository.findById(bookRatingDto.getBook())
                .orElseThrow(() ->
                        new RuntimeException("Boot not found")
        );

// busca el usuario en la BD usando el id que viene en el dto
        User user = userRepository.findById(bookRatingDto.getUser())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // construye la entidad BookRating con los datos del dto + libro y usuario encontrados
        return BookRating.builder()
                .ratingId(bookRatingDto.getRatingId())
                .book(book)
                .score(bookRatingDto.getScore())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }
    // Convierte una entidad BookRating en un BookRatingDto (para enviar al frontend)
    public BookRatingDto entityToDto(BookRating bookRating) {
        return BookRatingDto.builder()
                .ratingId(bookRating.getRatingId())
                .book(bookRating.getBook().getBookId())
                .score(bookRating.getScore())
                .createdAt(LocalDateTime.now())
                .user(bookRating.getUser().getUserId())
                .build();
    }
}
