package com.bookify.books.Bookify.shared.interfaces.userinterface;

import com.bookify.books.Bookify.model.dto.userdto.UpdateUserDto;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// contrato para gestionar usuarios en Bookify
public interface UserInterface {

    String userRegister(UserDTO userDTO);
    // registra un usuario nuevo

    UserDTO verifyCodeAndSave(String email, String inputCode);
    // verifica código enviado al email y, si es correcto, activa/guarda al usuario

    UserDTO getUser(String email);
    // devuelve el perfil del usuario por email

    String updatePasswordEmailSender(String email);
    // envía correo con instrucciones/código para actualizar contraseña

    Page<UserDTO> getAllUsers(Pageable pageable);
    // lista paginada de usuarios

    String userDelete(String email);
    // elimina o desactiva un usuario por email (devuelve mensaje)

    UserDTO userUpdate(String email, UpdateUserDto userDTO);
    // actualiza datos de perfil (nombre, username, aboutMe, etc.)
}
