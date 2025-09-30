package com.bookify.books.Bookify.shared.interfaces.bookinterfaces;

import com.bookify.books.Bookify.model.dto.bookdto.BookPageDTO;
import com.bookify.books.Bookify.model.entities.bookentities.BookPageEntity;

import java.io.IOException;
import java.util.Set;
// contrato para manejar páginas de un libro
public interface BookPageInterface {

    // Crea una nueva página en un libro específico usando su código identificador
    BookPageDTO createBookPage(BookPageDTO bookPageDTO, String bookIdentifierCode);

    // Devuelve todas las páginas de un libro usando su id
    Set<BookPageEntity> getAllPages(Long bookId);

    // Devuelve una página individual por su id, número de página y el id del libro
    BookPageDTO getIndividualPage(Long bookPageId, int pageNumber, Long bookId);

    // Extrae el contenido de una página para mostrarlo en el lector
    String extractContentPage(Long bookPageId);

    // Actualiza los datos de una página existente (puede lanzar IOException si hay problemas al manejar archivos)
    BookPageDTO updateBookPage(Long bookPageId, BookPageDTO bookPageDTO) throws IOException;

    // Elimina una página de un libro
    String deleteBookPage(BookPageDTO bookPageDTO);
}
