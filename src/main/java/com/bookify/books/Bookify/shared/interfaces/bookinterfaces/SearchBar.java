package com.bookify.books.Bookify.shared.interfaces.bookinterfaces;

import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBar { // contrato para la barra de búsqueda
    // Busca libros por título y devuelve los resultados paginados en formato BookCardDto
    Page<BookCardDto> searchBook(String title, Pageable pageable);
    // Busca libros filtrando por autor (username) y devuelve usuarios paginados en formato UserDTO
    Page<UserDTO> searchBookByAuthor(String username, Pageable pageable);
}
