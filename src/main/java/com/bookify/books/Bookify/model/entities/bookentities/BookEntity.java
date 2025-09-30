package com.bookify.books.Bookify.model.entities.bookentities;

import com.bookify.books.Bookify.constants.BookStatus;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "book")
public class BookEntity {
    @Id //anotacion para que java reconozca el id en la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY)//bookId es la clave primaria, autogenerada por la base de datos (usando IDENTITY).
    //las columnas de la tabla book.
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "publication_year")
    private LocalDateTime datePublication;

    @Column(name = "book_identifier_code")
    private String bookIdentifierCode;

    @Column(name = "price")
    private Double price;

    @Column(name = "category")
    private String category;

    @Column(name = "coverpage")
    private String filePathCoverPage;

    @Column(name = "privacy_status")
    private BookStatus status;

    //es un campo activo cuando un libro existe, para elimar el libro este campo llega false y así no se elimina en la bd
    @Column(name = "is_active")
    private Boolean isActive;

    // ----------------------------
    // Relación del libro con las páginas
    // ----------------------------

    // Relación Uno a Muchos: un libro puede tener varias páginas
    @OneToMany(
            mappedBy = "bookId",    // El dueño de la relación está en BookPageEntity (columna bookId)
            cascade = CascadeType.ALL,  // Si se guarda/actualiza/elimina un libro, se hace lo mismo con sus páginas
            orphanRemoval = true) // Si eliminas una página del set, también se elimina en la base de datos
    @JsonManagedReference(value = "book-bookPage") //
    // Evita bucles infinitos al convertir a JSON: este lado es el "ManagedReference es el principal" (Book -> Pages)
    private Set<BookPageEntity> bookPageEntity = new HashSet<>();

    // ----------------------------
    // Relación del autor con libro
    // ----------------------------
    @ManyToOne
    @JoinColumn(name = "author_id")// En la tabla "book" habrá una columna "author_id" que apunta a la tabla "user"
    @JsonIgnoreProperties({"books", "purchasedBooks"})//ignora todos los libros y no se hace una llamada infinita
    private User author;
    // el campo se llama autor para tener mejor lectura de los datos del libro, pero el "autor" sigue perteneciendo
    // a la tablausuarios.
}
