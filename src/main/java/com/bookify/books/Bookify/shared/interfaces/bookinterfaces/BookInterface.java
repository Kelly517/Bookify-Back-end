package com.bookify.books.Bookify.shared.interfaces.bookinterfaces;

import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.bookdto.DtoCrearLibro;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookInterface { // contrato para manejar libros en Bookify

    DtoLibro crearLibro(DtoCrearLibro dtoCrearLibro);// Crea un libro nuevo a partir del dto con datos básicos

    DtoLibro readBook (String bookIdentifierCode); // Busca un libro por su código identificador único

    Page<BookCardDto> getAllBooks(Pageable pageable);// Trae todos los libros en formato tarjeta (BookCardDto) con paginación

    List<BookCardDto> getTopRatedBooksOfWeek();// Trae los libros mejor puntuados de la semana

    Page<BookCardDto> getBooksByCategory(String category, Pageable pageable);// Trae libros de una categoría específica (también paginado)

    DtoLibro updateBook (DtoLibro dtoLibro);// Actualiza la información de un libro

    String deleteBook (String bookIdentifierCode);// Elimina un libro usando su código identificador

    Page<BookCardDto> getPurchasedBooks(Long userId, Pageable pageable);// Trae los libros comprados por un usuario específico (con paginación)

}
