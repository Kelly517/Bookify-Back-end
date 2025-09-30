package com.bookify.books.Bookify.model.entities.bookentities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity // Marca esta clase como entidad JPA (mapeada a tabla de la BD)
@Data // Lombok: genera getters, setters, equals, hashCode, toString
@NoArgsConstructor // Constructor vacío (necesario para JPA)
@AllArgsConstructor // Constructor con todos los atributos
@Builder // Permite construir objetos con el patrón Builder

@Table(name = "book_page") // Nombre de la tabla en la BD
public class BookPageEntity {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se genera automáticamente (auto-increment)
    private Long bookPageId; // Identificador único de cada página

    @Column(name = "page_title") // Título opcional de la página
    private String pageTitle;

    @Lob // Indica que es un campo de gran tamaño (Large Object)
    @Column(name = "page_content", columnDefinition = "LONGTEXT")
    private String pageContent; // Contenido completo de la página (texto largo)

    @Column(name = "page_number") // Número de la página dentro del libro
    private int pageNumber;

    @Column(name = "file_route") // ??Ruta de un archivo (ej: imágenes incrustadas o anexos)
    private String fileRoute;


    // ----------------------------
    // Relación con el libro al que pertenece
    // ----------------------------
    @ManyToOne
    @JoinColumn(name = "book_id")
    // En la tabla "book_page" hay una columna "book_id" que apunta a la tabla "book"
    @JsonBackReference(value = "book-bookPage")
    private BookEntity bookId;

    // ----------------------------
    // Métodos para comparar entidades
    // ----------------------------

    @Override
    public int hashCode() {
        return Objects.hash(bookPageId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;// Si son el mismo objeto, son iguales
        if (obj == null || getClass() != obj.getClass()) return false;// Si no es la misma clase, no son iguales
        BookPageEntity that = (BookPageEntity) obj;
        return Objects.equals(bookPageId, that.bookPageId); // Compara solo por el ID
    }
}
