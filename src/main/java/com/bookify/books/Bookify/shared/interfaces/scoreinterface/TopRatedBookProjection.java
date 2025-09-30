package com.bookify.books.Bookify.shared.interfaces.scoreinterface;

public interface TopRatedBookProjection {// proyección para obtener libros mejor valorados
    Long getBookId(); // Devuelve el id del libro
    Double getAverageScore();// Devuelve el puntaje promedio del libro
}
