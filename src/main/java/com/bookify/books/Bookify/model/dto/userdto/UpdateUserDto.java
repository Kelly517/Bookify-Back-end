package com.bookify.books.Bookify.model.dto.userdto;

import com.bookify.books.Bookify.annotations.ValidFormatUserName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {

    @NotEmpty(message = "The name can't be empty")
    private String name;// Nombre del usuario

    @NotEmpty(message = "The lastname can't be empty")
    private String lastname;// Apellido del usuario

    @ValidFormatUserName
    private String userName; // Nombre de usuario (validado con anotación personalizada)

    @NotEmpty(message = "Profile information can't be empty")
    private String aboutMe; // Información del perfil / descripción personal

}
// DTO usado para actualizar datos del perfil de usuario
// (nombre, apellido, username y descripción).
// No expone campos sensibles como email o password.