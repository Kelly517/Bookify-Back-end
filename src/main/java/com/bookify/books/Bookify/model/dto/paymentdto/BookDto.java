package com.bookify.books.Bookify.model.dto.paymentdto;

import com.bookify.books.Bookify.model.entities.userentities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {

    private String title;// Título del libro
    private String bookIdentifierCode;// Código identificador del libro
    private User author;// Autor del libro
}
//Este BookDto sirve para representar solo la información mínima de un libro
// dentro del contexto de pagos,
// de manera que las órdenes no carguen con toda la entidad completa.
