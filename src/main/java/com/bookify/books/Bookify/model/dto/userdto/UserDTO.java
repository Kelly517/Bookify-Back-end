package com.bookify.books.Bookify.model.dto.userdto;

import com.bookify.books.Bookify.annotations.ValidFormatUserName;
import com.bookify.books.Bookify.model.dto.bookdto.DtoLibro;
import com.bookify.books.Bookify.model.entities.bookentities.BookEntity;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long userId;

    private String profilePhoto;

    @NotEmpty(message = "The name can't be empty")
    private String name;// Nombre del usuario

    @NotEmpty(message = "The lastname can't be empty")
    private String lastname;

    @ValidFormatUserName
    private String userName;

    @NotEmpty(message = "The email can't be empty")
    @Email(message = "Invalid email. The email contains an incorrect format")//valida que el formato del email sea correcto
    private String email; // Correo del usuario (validado)

    private String aboutMe; // Descripción breve del usuario

    @NotEmpty(message = "The password can't be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Invalid password. Must contain a number, a capital letter and a character")//valida que el formato de la contraseña
    private String password; // Contraseña con validaciones de seguridad

    private List<BookEntity> purchasedBooks;// Libros comprados por el usuario

    private UserRole userRole; // Rol del usuario en el sistema (ej: AUTHOR, ADMIN)
    private List<DtoLibro> books; // Libros creados por el usuario

    // 👉 DTO usado para representar perfiles completos de usuario:
    // incluye datos personales, sociales, rol y libros asociados.
}
