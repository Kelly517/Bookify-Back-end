package com.bookify.books.Bookify.model.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecoveryPasswordDTO {
    private String email; // Correo del usuario que solicita el cambio
    private String newPassword;// Nueva contraseña propuesta
    private String confirmNewPassword; // Confirmación de la nueva contraseña
}
// DTO usado en el flujo de recuperación/cambio de contraseña.
// Garantiza que el usuario envíe el correo y valide dos veces la nueva clave.
