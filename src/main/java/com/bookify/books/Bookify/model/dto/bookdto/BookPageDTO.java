package com.bookify.books.Bookify.model.dto.bookdto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookPageDTO {
    private Long bookPageId;// ID interno de la página (no se expone en el JSON de la API)
    private String pageTitle;// Título de la página o capítulo
    private String pageContent; // Contenido en texto de la página
    private int pageNumber;// Número de página dentro del libro
    private String fileRoute; // Ruta de un archivo asociado (ej: imágenes o anexos)
    private Long bookId;// ID del libro al que pertenece esta página
}
