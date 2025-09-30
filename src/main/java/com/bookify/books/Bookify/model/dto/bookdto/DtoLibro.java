package com.bookify.books.Bookify.model.dto.bookdto;

import com.bookify.books.Bookify.annotations.ValidImageExtension;
import com.bookify.books.Bookify.constants.BookStatus;
import com.bookify.books.Bookify.model.entities.bookentities.BookPageEntity;
import com.bookify.books.Bookify.model.entities.userentities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoLibro {

    private Long bookId;// Identificador único del libro

    @NotEmpty(message = "The name can't be empty")
    private String title;// Título del libro (obligatorio)

    @NotEmpty(message = "Short description can't be empty")
    @Size(min = 20, max = 100, message = "Short message must be between 20 and 100 characters")
    private String shortDescription;// Descripción corta (20 a 100 caracteres)

    @NotEmpty(message = "Description can't be empty")
    @Size(min = 100, max = 2000, message = "The description can't be longer than 2000 characters or shorter than 100 characters")
    private String description;// Descripción larga (100 a 2000 caracteres)

    //@PastOrPresent -> valida que la fecha sea pasado o presente
    @PastOrPresent(message = "The date can't be later than the current date")
    private LocalDateTime datePublication;// Fecha de publicación (no puede ser futura)

    private Double price;// Precio del libro

    @NotNull(message = "Category is required")
    private String category; // Categoría o género del libro

    @NotNull
    private BookStatus status;// Estado del libro (ej: PUBLISHED, DRAFT, PRIVATE)

    @NotNull
    private Boolean isActive;// Activo/inactivo (soft delete)

    @NotEmpty(message = "The identifier can't be empty")
    private String bookIdentifierCode;// Código identificador único

    @ValidImageExtension // valida que la portada del libro tenga un formato png o jpg
    private String filePathCoverPage;// Ruta de la portada (validada con anotación custom)

    @Valid
    private Set<BookPageEntity> bookPageEntity;// Conjunto de páginas asociadas al libro

    private User author;// Autor del libro (actualmente se expone entidad User)
}
