package com.bookify.books.Bookify.service.userservices;

import com.bookify.books.Bookify.cache.VerificationCodeService;
import com.bookify.books.Bookify.components.SuperAdmin;
import com.bookify.books.Bookify.email.service.VerificationEmailService;
import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.model.dto.userdto.UpdateUserDto;
import com.bookify.books.Bookify.model.dto.userdto.UserDTO;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.shared.interfaces.securityinterfaces.GenerateUniqueCode;
import com.bookify.books.Bookify.shared.interfaces.userinterface.UserInterface;
import com.bookify.books.Bookify.model.entities.userentities.UserRole;
import com.bookify.books.Bookify.repository.userrepositories.UserRoleRepository;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.model.mappers.user.UserDTOsMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bookify.books.Bookify.components.SuperAdmin.SUPER_ADMIN_EMAIL;

/**
 * Servicio encargado de crear toda la lógica para registrar a los usuarios
 * {@link SuperAdmin}
 * {@link UserInterface}
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserInterface, GenerateUniqueCode {
    // Repositorios y servicios usados
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationEmailService verificationEmailService;
    private final VerificationCodeService verificationCodeService;
    private final UserDTOsMapper userDTOsMapper;

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class.getName());

    @Override
    public String userRegister(UserDTO userDTO) {
        // 1) Buscar rol por defecto
        Optional<UserRole> roleSearch = userRoleRepository.findByRoleName("USER");

        // 2) Validaciones: que el email y el username no existan
        userExists(userDTO.getEmail());
        createUserName(userDTO.getUserName());
    // 3) Encriptar contraseña
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())); //Envía la contraseña protegida usando encoder para codificarla
        // 4) Asignar rol si existe
        roleSearch.ifPresent(userDTO::setUserRole); //Busca si el rol buscado existe, y luego lo envía al usuario. Por defecto es user
        // 5) Convertir DTO -> Entidad (para persistencia)
        User user = userDTOsMapper.convertDTOToEntity(userDTO); //Se mappea la entidad de usuario al DTO de usuario para guardar en la base de datos
        // 6) Generar código de verificación y enviarlo por email
        String code = generateUniqueId();

        verificationEmailService.sendVerificationEmail(userDTO.getEmail(), code);
        // 7) Guardar en cache: código y el propio usuario (para confirmar después)
        verificationCodeService.saveCode(userDTO.getEmail(), code);
        verificationCodeService.saveUser(userDTO);
        // 8) Responder (nota: hay typo en "Conde")
        return "Conde sent. Please review the email: " + user.getEmail();
    }
    // Valida si existe un usuario con ese email (si existe, corta el registro)
    private void userExists(String email) {
        Optional<User> userSearch = userRepository.findByEmail(email);

        if (userSearch.isPresent()) {
            LOG.error("User already exists");
            throw new UserExistsException("User already exists");
        }
    }
    // Valida si el username ya existe (si existe, corta el registro)
    private void createUserName(String userName) {
        Optional<User> findUserName = userRepository.findByUserName(userName);

        if (findUserName.isPresent()) {
            LOG.error("User name already exists");
            throw new UserExistsException("User name already exists");
        }
    }

    @Override
    public UserDTO verifyCodeAndSave(String email, String inputCode) {
        // 1) Obtener el código guardado (cache/redis) para ese email
        String redisCode = verificationCodeService.getCode(email).orElseThrow(() ->
                new IllegalArgumentException("Code not found"));
        // 2) Obtener el usuario que quedó en espera de verificación
        Optional<UserDTO> user = verificationCodeService.getUser(email);
        // 3) Validar código
        if (redisCode.isEmpty() || !redisCode.equals(inputCode)) {
            throw new RuntimeException("¡Error! Invalid code, try again");
        }
        // 4) Si hay usuario en cache, persistir en BD y limpiar el código
        if (user.isPresent()) {
            UserDTO userDTO = user.get();

            User userEntity = userDTOsMapper.convertDTOToEntity(userDTO);
            User savedUSer = userRepository.save(userEntity);
            verificationCodeService.deleteCode(email);
            // 5) Devolver DTO confirmado desde la entidad guardada
            return userDTOsMapper.convertEntityToDTO(savedUSer);
        }
        // 6) Si no hay usuario en cache o datos inválidos
        throw new RuntimeException("Invalid user. User doesn't exists or invalid data");
    }

    @Override
    public UserDTO getUser(String email) {
        // Busca por email o lanza excepción
        User getUserWithEmail = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserExistsException("User not found " + email));
        // Mapea entidad -> DTO y devuelve
        return UserDTOsMapper.convertEntityToDTO(getUserWithEmail);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        // Paginación + map a DTO
        return userRepository
                .findAll(pageable)
                .map(UserDTOsMapper::convertEntityToDTO);
    }

    @Override
    public String updatePasswordEmailSender(String email) {
        // 1) Validar que exista el usuario
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserExistsException("User not found " + email));
        // 2) Generar código y enviar correo con enlace (ruta del front)
        String code = generateUniqueId();
        verificationEmailService.setUpdateEmail(user.getEmail(), "http://localhost:5173/update/password/" + email);
        // 3) Guardar código (para validar luego)
        verificationCodeService.saveCode(user.getEmail(), code);

        return "Email sent to " + email;
    }

    @Override
    public String userDelete(String email) {
        // 1) Buscar usuario por email
        Optional<User> getUserWithEmail = userRepository.findByEmail(email);
        // 2) No permitir borrar al Super Admin
        if (getUserWithEmail.isPresent() && !SUPER_ADMIN_EMAIL.equals(email)) {
            userRepository.delete(getUserWithEmail.get());
            LOG.info("This user has ben deleted: {}", getUserWithEmail);
            return "Deleted";
        } else {
            LOG.error("Cannot delete Super Admin: {}", getUserWithEmail);
            throw new UserExistsException("Cannot delete Super Admin");
        }
    }

    @Override
    public UserDTO userUpdate(String email, UpdateUserDto userDTO) {
        // 1) Buscar usuario
        Optional<User> userSearch = userRepository.findByEmail(email);

        if (userSearch.isEmpty()) {
            LOG.error("User doesn't exists, {}", userSearch);
            throw new UserExistsException("User doesn't exists");
        }
        // 2) Setear nuevos datos (solo perfil visible)
        User userUpdated = userSearch.get();
        setUpdatedData(userUpdated, userDTO);
        // 3) Guardar cambios
        User userSave = userRepository.save(userUpdated);
        LOG.info("User updated: {}", userUpdated);
        // 4) Devolver DTO actualizado
        return userDTOsMapper.convertEntityToDTO(userSave);
    }
    // Pasa los datos del DTO al usuario (solo nombre, apellido, username, aboutMe)
    private void setUpdatedData(User userUpdated, UpdateUserDto userDTO) {
        userUpdated.setName(userDTO.getName());
        userUpdated.setLastname(userDTO.getLastname());
        userUpdated.setUserName(userDTO.getUserName());
        userUpdated.setAboutMe(userDTO.getAboutMe());
    }
}
