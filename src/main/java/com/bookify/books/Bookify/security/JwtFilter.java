package com.bookify.books.Bookify.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro personalizado que intercepta todas las solicitudes HTTP entrantes para:
 * - Extraer el token JWT del encabezado Authorization.
 * - Validar el token y extraer sus claims (email y rol).
 * - Autenticar al usuario si el token es válido.
 * <p>
 * Este filtro se ejecuta una vez por cada solicitud gracias a {@link OncePerRequestFilter}.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Utilidad para generar, validar y extraer datos de tokens JWT.
     * Usado para verificar el token enviado por el cliente.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Servicio que carga los detalles del usuario desde la base de datos.
     * Necesario para crear la autenticación en el contexto de Spring Security.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Método principal del filtro que se ejecuta una vez por solicitud.
     * Extrae y valida el token JWT, y si es válido, establece la autenticación
     * en el contexto de seguridad de Spring.
     *
     * @param request     Solicitud HTTP entrante.
     * @param response    Respuesta HTTP que se enviará al cliente.
     * @param filterChain Cadena de filtros que deben seguir ejecutándose.
     * @throws ServletException En caso de error en el filtro.
     * @throws IOException      En caso de error de lectura/escritura.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el encabezado "Authorization"
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;
        String role = null;

        // Validar si el encabezado tiene un token Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Elimina el prefijo "Bearer "
            Claims claims = jwtUtil.extractClaims(token); // Extrae los claims del token
            email = claims.getSubject(); // Email del usuario
            role = claims.get("role", String.class); // Rol del usuario
        }

        // Si el email está presente y el usuario aún no ha sido autenticado
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Validar el token
            if (jwtUtil.validateToken(token, userDetails.getUsername(), role)) {
                // Crear el token de autenticación y establecerlo en el contexto
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Token validation failed " + token + role + userDetails.getUsername());
            }
        }

        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
