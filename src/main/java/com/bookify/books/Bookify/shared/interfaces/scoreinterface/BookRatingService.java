package com.bookify.books.Bookify.shared.interfaces.scoreinterface;

import com.bookify.books.Bookify.model.dto.bookdto.BookRatingDto;
// contrato para manejar calificaciones de libros
public interface BookRatingService {
    // Crea una nueva calificación para un libro
    // Recibe un BookRatingDto con la info del rating y devuelve el mismo dto confirmado
    BookRatingDto createRating(BookRatingDto bookRatingDto);
}
