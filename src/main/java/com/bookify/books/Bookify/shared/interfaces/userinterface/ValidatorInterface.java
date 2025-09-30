package com.bookify.books.Bookify.shared.interfaces.userinterface;

import com.bookify.books.Bookify.model.dto.bookdto.BookPageDTO;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;

import java.util.HashSet;

public interface ValidatorInterface {// contrato para validar datos antes de guardar

    default void validators(BookPageDTO bookPageDTO, BookEntity bookEntity) {
        // Si el libro no tiene páginas aún, se inicializa con un HashSet vacío
        if (bookEntity.getBookPageEntity() == null) {
            bookEntity.setBookPageEntity(new HashSet<>());
        }

        // Si el título de la página es nulo o está vacío, se lanza un error
        if (bookPageDTO.getPageTitle() == null || bookPageDTO.getPageTitle().isEmpty()) {
            throw new RuntimeException("Title cannot be empty");
        }
    }
}
