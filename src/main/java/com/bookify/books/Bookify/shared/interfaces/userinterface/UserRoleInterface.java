package com.bookify.books.Bookify.shared.interfaces.userinterface;


import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.model.dto.userdto.UserRoleDTO;

public interface UserRoleInterface {// contrato para manejar roles de usuario
    // crea un nuevo rol en el sistema (ej: ADMIN, USER, AUTHOR)
    UserRoleDTO createNewRole(UserRoleDTO userRoleDTO);

    // asigna un rol a un usuario identificado por su email
    User assignUserRole(String email, String roleName);
}
