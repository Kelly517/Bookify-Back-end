package com.bookify.books.Bookify.shared.interfaces.userinterface;

import com.bookify.books.Bookify.model.dto.userdto.RecoveryPasswordDTO;
// contrato para recuperar/actualizar contraseñas
public interface RecoveryPasswordInterface {
    // Actualiza la contraseña de un usuario
    // Recibe un DTO con el email, la nueva contraseña y su confirmación
    // Devuelve un String (mensaje de éxito o error)
    String UpdatePassword(RecoveryPasswordDTO recoveryPasswordDTO);
}
