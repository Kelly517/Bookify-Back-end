package com.bookify.books.Bookify.model.entities.userentities;

import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity // Esta clase se convierte en una tabla de la BD
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")

public class User {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Auto-incremental en la BD
    @Column(name = "id")
    private Long userId;// Identificador único del usuario
    // ----------------------------
    // Información básica del perfil
    // ----------------------------
    @Column(name = "profile_photo")
    private String profilePhoto;// Ruta o URL de la foto de perfil

    @Column(name = "name")
    private String name;// Nombre real del usuario

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String userName; // Nombre de usuario

    @Column(name = "about_me")
    private String aboutMe;// Biografía o descripción personal

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;


    // ----------------------------
    // Relación con libros comprados
    // ----------------------------
    @ManyToMany
    @JoinTable(
            name = "user_purchased_books",// Tabla intermedia usuario-libro
            joinColumns = @JoinColumn(name = "user_id"),// FK al usuario
            inverseJoinColumns = @JoinColumn(name = "book_id")// FK al libro
    )
    private List<BookEntity> purchasedBooks;// Libros que el usuario compró

    // ----------------------------
    // Relación con roles
    // ----------------------------
    @ManyToOne
    @JoinColumn(name = "role_id")// Cada usuario tiene un rol (lector, admin)
    private UserRole userRole;

    // ----------------------------
    // Relación con libros creados (si es autor)
    // ----------------------------
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"author"}) // evita el loop infinito
    private List<BookEntity> books; // Libros escritos por el usuario
}
