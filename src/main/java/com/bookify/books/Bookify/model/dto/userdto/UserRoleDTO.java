package com.bookify.books.Bookify.model.dto.userdto;

import com.bookify.books.Bookify.model.entities.userentities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // Lombok: genera getters, setters, equals, hashCode y toString
@AllArgsConstructor // Constructor con todos los atributos
@NoArgsConstructor // Constructor vacío
public class UserRoleDTO {

    @JsonIgnore
    private Long roleId;// ID interno del rol (no se expone en JSON para seguridad)

    @NotEmpty(message = "The role name can't be empty")
    @Pattern(regexp = "^[A-Z]+$", message = "Role name can't be in lower case")
    private String roleName;
    // Nombre del rol (ej: ADMIN, AUTHOR, READER), validado para que no esté vacío y siempre en mayúsculas

    private List<User> userEntities;
    // Lista de usuarios que tienen este rol (actualmente expone entidades directamente)
}
//UserRoleDTO sirve para exponer y validar los roles de usuario en Bookify, asegurando consistencia en su escritura
// y permitiendo relacionar a los usuarios con cada rol.