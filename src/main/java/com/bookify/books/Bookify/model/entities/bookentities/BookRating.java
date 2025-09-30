package com.bookify.books.Bookify.model.entities.bookentities;

import com.bookify.books.Bookify.model.entities.userentities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor // Constructor con todos los atributos
@NoArgsConstructor // Constructor vacío (requerido por JPA)
@Builder // Permite construir objetos con el patrón Builder
@Table(name = "book_rating")

public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Se genera automáticamente en la BD
    private Long ratingId; // Identificador único de la calificación

    // ----------------------------
    // Relación con el libro calificado
    // ----------------------------
    @ManyToOne(fetch = FetchType.LAZY)// Varios ratings pueden pertenecer a un mismo libro
    @JoinColumn(name = "book_id", nullable = false) // Columna "book_id" en la tabla book_rating apunta a la tabla book
    private BookEntity book;

    @Column(name = "score") // Puntuación dada al libro (ej: 1 a 5 estrellas)
    private Integer score;

    @Column(name = "created_at")// Fecha en que se hizo la calificación
    private LocalDateTime createdAt;


    // ----------------------------
    // Relación con el usuario que calificó
    // ----------------------------
    @ManyToOne(fetch = FetchType.LAZY)// Varios ratings pueden pertenecer a un mismo usuario
    @JoinColumn(name = "user_id", nullable = false)    // Columna "user_id" en la tabla book_rating apunta a la tabla user

    private User user;
}
