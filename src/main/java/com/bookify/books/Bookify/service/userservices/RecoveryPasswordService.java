package com.bookify.books.Bookify.service.userservices;

import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.model.dto.userdto.RecoveryPasswordDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.shared.interfaces.userinterface.RecoveryPasswordInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
// Servicio que maneja la lógica para recuperar y actualizar contraseñas
public class RecoveryPasswordService implements RecoveryPasswordInterface {

    private final PasswordEncoder passwordEncoder; // Encripta las contraseñas antes de guardarlas
    private final UserRepository userRepository;// Acceso a la base de datos de usuarios
    private final static Logger LOG = LoggerFactory.getLogger(RecoveryPasswordService.class.getName()); // Logger para registrar eventos de este servicio


    @Override // se sobreescribe y contiene la lógica
    public String UpdatePassword(RecoveryPasswordDTO recoveryPasswordDTO) {

        Optional<User> user = userRepository.findByEmail(recoveryPasswordDTO.getEmail());//busca por email

        // Validar si el usuario existe y que las contraseñas coincidan
        if (user.isPresent() &&
                validateMatchesPasswords(
                        recoveryPasswordDTO.getNewPassword(),
                        recoveryPasswordDTO.getConfirmNewPassword())) {

            // Encriptar la nueva contraseña
            String encodedNewPassword = encodePassword(recoveryPasswordDTO.getNewPassword());
            user.get().setPassword(encodedNewPassword);// Actualizar el campo password del usuario
            userRepository.save(user.get());// Guardar los cambios en la base de datos
    // Registrar log de éxito
            LOG.info("Password updated for user: {}", recoveryPasswordDTO.getEmail());
            return "PASSWORD UPDATED";
        }
        // Si las contraseñas no coinciden, lanzar error
        LOG.error("The passwords does not match. They must be the same to continue.");
        throw new UserExistsException("The passwords does not match. They must be the same to continue.");
    }

    // Metodo auxiliar: encripta la contraseña con PasswordEncoder
    private String encodePassword(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }
    // Metodo auxiliar: compara si las dos contraseñas coinciden
    private Boolean validateMatchesPasswords(String newPassword, String confirmNewPassword) {
        return newPassword.equals(confirmNewPassword);
    }
}
