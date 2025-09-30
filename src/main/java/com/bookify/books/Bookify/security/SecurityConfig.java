package com.bookify.books.Bookify.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de seguridad de la aplicación.
 * Define las políticas de acceso a rutas según los roles de usuario,
 * configura el filtro JWT, el manejo de sesiones, y otros aspectos clave
 * de seguridad como CORS, CSRF y manejo de excepciones.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Filtro JWT personalizado para extraer, validar y autenticar al usuario
     * a partir del token incluido en la cabecera Authorization.
     */
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Servicio que carga los detalles del usuario desde la base de datos.
     * Es usado para construir la autenticación en Spring Security.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configuración personalizada de CORS (Cross-Origin Resource Sharing).
     * Permite el control sobre qué dominios pueden hacer solicitudes a esta API.
     */
    @Autowired
    private CorsConfig corsConfig;

    /**
     * Bean encargado de encriptar contraseñas usando BCrypt.
     * Es fundamental para el almacenamiento seguro de contraseñas en la base de datos.
     *
     * @return Implementación de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para obtener el AuthenticationManager, necesario para autenticar usuarios
     * en el proceso de login usando el contexto de Spring Security.
     *
     * @param authenticationConfiguration Configuración de autenticación provista por Spring Boot.
     * @return Instancia de {@link AuthenticationManager}.
     * @throws Exception En caso de error de configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad para:
     * - Restringir rutas según el rol del usuario.
     * - Deshabilitar CSRF (ya que usamos JWT).
     * - Manejar excepciones como acceso denegado.
     * - Crear sesiones sin estado (STATELESS).
     * - Incluir el filtro JWT antes del filtro por defecto de autenticación.
     * - Aplicar configuración de CORS.
     *
     * @param httpSecurity Objeto de configuración de seguridad HTTP.
     * @return La cadena de filtros de seguridad ya construida.
     * @throws Exception En caso de fallo al construir la configuración.
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/bookify/writer/**").hasAuthority("WRITER")  // Rutas solo accesibles por WRITER
                        .requestMatchers("/bookify/user/**").hasAuthority("USER")      // Rutas solo accesibles por USER
                        .requestMatchers("/**").permitAll()                             // El resto accesible sin autenticación
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable) // CSRF deshabilitado ya que usamos JWT
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Denied access");
                        })
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin sesiones, todo vía JWT
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Inserta el filtro antes del filtro de login
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())); // Aplica configuración CORS

        return httpSecurity.build();
    }
}
