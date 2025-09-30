package com.bookify.books.Bookify.service.userservices;

import com.bookify.books.Bookify.exceptions.userexceptions.UserExistsException;
import com.bookify.books.Bookify.shared.interfaces.userinterface.LoginInterface;
import com.bookify.books.Bookify.model.entities.userentities.User;
import com.bookify.books.Bookify.repository.userrepositories.UserRepository;
import com.bookify.books.Bookify.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//clase que contiene la lógica de negocio para iniciar sesión.
public class LoginService implements LoginInterface {

    //Se declara como objeto
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

    /**
     * El metodo login pide el email y la contraseña
     * despues de validar que exista el usuario y que la contraseña sea correcta
     * usa un authentication manager para verificar el rol del usuario
     * Luego cuando toda la información es correcta, genera el token y lo envía al front end
     * @param email
     * @param password
     * @return token
     */
    @Override
    public String login(String email, String password) {
        User searchUserToLogin = userRepository // crea un atributo User y se busca en la base de datos
                .findByEmail(email) //En la base de datos se busca por el email
                .orElseThrow(() -> //Esto es la excepcion, el error
                        new UserExistsException("El usuario no existe"));

        Long userId = searchUserToLogin.getUserId(); // El id también se envía al front end en el token

        if (!passwordEncoder.matches(password, searchUserToLogin.getPassword())) { //El password encoder toma las dos contraseñas codificadas y las compara
            LOG.error("Incorrect credentials");
            throw new UserExistsException("Incorrect credentials"); //Lanza error si las contraseñas no coinciden
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String role = generateRole(authentication); //Usando el authentication de antes, cuando ya se loggea, busca el rol del usuario. Si no existe, no permite el inicio de sesión
        LOG.info("Usuario verificado: {}", email);

        return jwtUtil.generateToken(userId, email, role); //Aquí se pide el JWT (Json web Token Util) y luego se retorna al front ya creado
    }

    /**
     * Metodo auxiliar para buscar el rol del usuario
     * @param authentication
     * @return rol o error
     */

    private String generateRole(Authentication authentication) {
        return authentication.getAuthorities().stream()// Recibe un authentication y lo recorre
                .map(GrantedAuthority::getAuthority) // Busca el rol del usuario
                .findFirst() // Toma el primero que encuentra
                .orElseThrow(() -> new RuntimeException("El usuario no tiene rol"));
    }
}
