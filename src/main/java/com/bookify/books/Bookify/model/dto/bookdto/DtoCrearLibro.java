package com.bookify.books.Bookify.model.dto.bookdto;

import com.bookify.books.Bookify.constants.BookStatus;
import com.bookify.books.Bookify.model.entities.userentities.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data// Lombok: genera getters, setters, equals, hashCode y toString
@AllArgsConstructor// Constructor con todos los atributos
@NoArgsConstructor // Constructor vacío
@Builder // Permite construir instancias con el patrón Builder
public class DtoCrearLibro {

    @NotEmpty(message = "The name can't be empty")
    private String title; // Título del libro (obligatorio)

    @NotEmpty(message = "Short description can't be empty")
    // @Size -> verifica que el tamaño del texto cumpla con el min y max que se pide
    @Size(min = 20, max = 100, message = "Short message must be between 20 and 100 characters")
    private String shortDescription;// Descripción corta (entre 20 y 100 caracteres)

    @NotEmpty(message = "Description can't be empty")
    @Size(min = 100, max = 2000, message = "Description can't be longer than 2000 characters or shorter than 100 characters")
    private String description; // Descripción larga (100 a 2000 caracteres)

    // anotacion para validar que el numero no esté po debajo del min solicitado
    @Min(value = 0, message = "The price can't be negative")
    private Double price;// Precio del libro (no puede ser negativo)

    @NotNull(message = "Category is required")
    private String category;// Categoría o género del libro

    private User user;// Autor del libro (se recibe como entidad, aunque lo ideal es un UserDto con solo ID)

    private BookStatus status;// Estado del libro (ej: PUBLISHED, DRAFT, PRIVATE)
}
