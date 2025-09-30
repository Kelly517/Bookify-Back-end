package com.bookify.books.Bookify.model.mappers.book;

import com.bookify.books.Bookify.model.dto.bookdto.BookCardDto;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import lombok.Builder;
import org.springframework.stereotype.Component;
// buider = construir
@Builder //hace que se construya el objeto que le pida
@Component
public class BookCardDTOMapper { // clase donde se transforma la entidad en dto
    //Este mapper es de salida para mostrar
    //este metodo recibe la entidad book y puntuacion del libro
    public static BookCardDto convertBookToDTO(BookEntity bookEntity, Double averageScore) {
        //retorna un dto con la orden de construir
        return BookCardDto.builder()
                //y se construye pasando la informacion de bookEntity a cada atributo del dto
                .bookId(bookEntity.getBookId())//trae el id de la entidad hacia el dto
                .bookIdentifierCode(bookEntity.getBookIdentifierCode())
                .coverPage(bookEntity.getFilePathCoverPage())
                .title(bookEntity.getTitle())
                .shortDescription(bookEntity.getShortDescription())
                .description(bookEntity.getDescription())
                .price(bookEntity.getPrice())
                .category(bookEntity.getCategory())
                .status(bookEntity.getStatus())
                .author(bookEntity.getAuthor())
                .averageScore(averageScore)
                .build();// construye y devuelve el DTO final
    }
    //Este metodo recibe un DtoLibro que es más completo
    // y lo transforma en un BookCardDto, una versión reducida que es la bookcard
    public static BookCardDto BookEntityDtoToCard(DtoLibro dtoLibro) {
        return BookCardDto.builder()
                .bookId(dtoLibro.getBookId())
                .bookIdentifierCode(dtoLibro.getBookIdentifierCode())
                .coverPage(dtoLibro.getFilePathCoverPage())
                .title(dtoLibro.getTitle())
                .shortDescription(dtoLibro.getShortDescription())
                .description(dtoLibro.getDescription())
                .price(dtoLibro.getPrice())
                .author(dtoLibro.getAuthor())
                .category(dtoLibro.getCategory())
                .status(dtoLibro.getStatus())
                .build();// construye y devuelve el DTO final
    }
}
