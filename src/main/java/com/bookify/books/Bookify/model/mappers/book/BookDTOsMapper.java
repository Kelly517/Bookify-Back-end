package com.bookify.books.Bookify.model.mappers.book;

import com.bookify.books.Bookify.model.dto.bookdto.DtoCrearLibro;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
@Builder
// clase donde se hacen las conversiones entre entidad y dto
public class BookDTOsMapper {
    // Este metodo convierte un DtoLibro en una entidad BookEntity (para guardar en la BD)
    public static BookEntity convertBookDTOToEntity(DtoLibro dtoLibro) {
        return BookEntity.builder()
                .bookId(dtoLibro.getBookId())
                .title(dtoLibro.getTitle())
                .shortDescription(dtoLibro.getShortDescription())
                .description(dtoLibro.getDescription())
                .datePublication(dtoLibro.getDatePublication())
                .price(dtoLibro.getPrice())
                .bookIdentifierCode(dtoLibro.getBookIdentifierCode())
                .filePathCoverPage(dtoLibro.getFilePathCoverPage())
                .bookPageEntity(dtoLibro.getBookPageEntity())
                .author(dtoLibro.getAuthor())
                .category(dtoLibro.getCategory())
                .status(dtoLibro.getStatus())
                .isActive(true)
                .build();
    }
    // Este metodo convierte una entidad BookEntity en un DtoLibro (para enviar al frontend)
    public static DtoLibro convertBookEntityToDTO(BookEntity bookEntity) {
        return DtoLibro.builder()
                .bookId(bookEntity.getBookId())
                .title(bookEntity.getTitle())
                .shortDescription(bookEntity.getShortDescription())
                .description(bookEntity.getDescription())
                .datePublication(bookEntity.getDatePublication())
                .price(bookEntity.getPrice())
                .bookIdentifierCode(bookEntity.getBookIdentifierCode())
                .filePathCoverPage(bookEntity.getFilePathCoverPage())
                .bookPageEntity(bookEntity.getBookPageEntity())
                .author(bookEntity.getAuthor())
                .category(bookEntity.getCategory())
                .status(bookEntity.getStatus())
                .isActive(true)
                .build();
    }
    // Genera la fecha de creación del libro (fecha y hora actual)
    private LocalDateTime creationDateBookGenerator() {
        return LocalDateTime.now();
    }
    // Genera un código identificador aleatorio para el libro
    private String bookIdentifierCodeGenerator() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BKF-");// prefijo fijo para identificar libros
        for (int i = 0; i < 9; i++) {
            int randomIndex = secureRandom.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));// agrega caracteres aleatorios
        }

        return stringBuilder.toString();// retorna el código final
    }
    // Este metodo recibe un DtoCrearLibro (lo que manda el usuario al crear un libro)
    // y lo transforma en un DtoLibro, agregando datos que se generan automáticamente
    public DtoLibro createBookDto(DtoCrearLibro dtoCrearLibro) {
        return DtoLibro.builder()
                .title(dtoCrearLibro.getTitle())
                .shortDescription(dtoCrearLibro.getShortDescription())
                .description(dtoCrearLibro.getDescription())
                .datePublication(creationDateBookGenerator())
                .price(dtoCrearLibro.getPrice())
                .category(dtoCrearLibro.getCategory())
                .status(dtoCrearLibro.getStatus())
                .author(dtoCrearLibro.getUser())
                .bookIdentifierCode(bookIdentifierCodeGenerator())
                .build();
    }

}
