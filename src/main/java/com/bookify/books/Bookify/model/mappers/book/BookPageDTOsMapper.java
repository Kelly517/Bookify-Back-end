package com.bookify.books.Bookify.model.mappers.book;

import com.bookify.books.Bookify.model.dto.bookdto.BookPageDTO;
import com.bookify.books.Bookify.model.entities.bookentities.BookPageEntity;
import com.bookify.books.Bookify.repository.bookrepositories.BookRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Builder
// clase que convierte entre entidad de página y su dto
public class BookPageDTOsMapper {

    @Autowired
    private BookRepository bookRepository; // repositorio de libros

    // Convierte un BookPageDTO en una entidad BookPageEntity (para guardar en BD)
    public BookPageEntity convertDtoToEntity(BookPageDTO bookPageDTO) {
        return BookPageEntity.builder()
                .bookPageId(bookPageDTO.getBookPageId())
                .pageTitle(bookPageDTO.getPageTitle())
                .pageContent(bookPageDTO.getPageContent())
                .pageNumber(bookPageDTO.getPageNumber())
                .fileRoute(bookPageDTO.getFileRoute())
                .build();
    }

    // Convierte una entidad BookPageEntity en un DTO
    //metodo de salida, para mostrar en el front
    public BookPageDTO convertEntityToDto(BookPageEntity bookPageEntity) {
        return BookPageDTO.builder()
                .bookPageId(bookPageEntity.getBookPageId())
                .pageTitle(bookPageEntity.getPageTitle())
                .pageContent(bookPageEntity.getPageContent())
                .pageNumber(bookPageEntity.getPageNumber())
                .fileRoute(bookPageEntity.getFileRoute())
                .build();
    }
}
