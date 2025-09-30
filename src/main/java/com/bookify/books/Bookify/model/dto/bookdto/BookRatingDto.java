package com.bookify.books.Bookify.model.dto.bookdto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class BookRatingDto {
    private Long ratingId;// Identificador único de la calificación

    /** anotaciones + mensajes de respuesta de la validacion
     *@NotEmpty -> valida que el campo no sea vacio  **/

    @NotEmpty(message = "libro id no encontrado")
    private Long book;// ID del libro al que pertenece la calificación

    @NotEmpty(message = "El libro necesita una puntuación")
    private Integer score;// Puntaje otorgado 1 a 5 estrellas

    @NotEmpty(message = "El libro debe tener fecha de la creación de la puntuación")
    private LocalDateTime createdAt;// Fecha en que se hizo la calificación

    @NotEmpty(message = "El libro necesita un usuario")
    private Long user; // ID del usuario que calificó
}
