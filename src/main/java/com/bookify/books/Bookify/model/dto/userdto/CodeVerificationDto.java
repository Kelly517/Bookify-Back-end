package com.bookify.books.Bookify.model.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CodeVerificationDto {
    private String email; // Correo del usuario que recibe/verifica el código
    private String code; // Código de verificación enviado al correo
}
//  DTO usado para validar un código de verificación asociado a un correo.
// Sirve en flujos como confirmación de cuenta.