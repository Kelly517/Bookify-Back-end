package com.bookify.books.Bookify.model.dto.bookdto;

import com.bookify.books.Bookify.constants.BookStatus;
import com.bookify.books.Bookify.model.entities.userentities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Builder// Lombok: permite construir el objeto con patrón Builder
@AllArgsConstructor// Constructor con todos los argumentos
@NoArgsConstructor// Constructor vacío
@Data// Lombok: genera getters, setters, equals, hashCode y toString
public class BookCardDto {
    private Long bookId; // ID del libro (clave principal en la BD)
    private String bookIdentifierCode;// Código único (ej: ISBN interno)
    private String coverPage;// Ruta o URL de la portada
    private String title;// Título del libro
    private String shortDescription;// Descripción corta (para la tarjeta)
    private String description;// Descripción completa
    private Double price;// Precio del libro
    private String category;// Categoría o género
    private User author;// Autor del libro (actualmente usando entidad User)
    private BookStatus status;// Estado del libro (
    private Double averageScore;// Promedio de calificación (ratings)
}
