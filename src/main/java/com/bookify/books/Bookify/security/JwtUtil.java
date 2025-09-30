package com.bookify.books.Bookify.security;

import com.bookify.books.Bookify.configuration.EnvConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Clase utilitaria para generar, validar y extraer información de tokens JWT.
 * Los tokens se firman usando el algoritmo HS512 y una clave secreta almacenada en variables de entorno.
 * <p>
 * Este token se utiliza para autenticar a los usuarios y controlar sus permisos mediante el rol.
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta para firmar y validar los tokens JWT.
     * Se obtiene desde el archivo .env a través de la clase {@link EnvConfig}.
     */
    private final SecretKey secretKey = new SecretKeySpec(
            EnvConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS512.getJcaName()
    );

    /**
     * Genera un token JWT (Json Web Token) válido por 1 hora, que contiene el email, el rol del usuario y el id.
     *
     * @param email    El email del usuario autenticado.
     * @param userRole El rol del usuario (por ejemplo: ADMIN, USER).
     * @return Un token JWT firmado con la clave secreta.
     */
    public String generateToken(Long userId, String email, String userRole) { //Aquí se pide el token, y luego se envía
        Date now = new Date(); //Exactamente en el momento en que se pide el token, se obtiene la hora de petición
        Date tokenExpiration = new Date(now.getTime() + 3600000); //A esa hora de petición se le suma una hora, que será la hora a la que muere el token

        return Jwts.builder() //Construye el token
                .setSubject(email) //Sujeto = usuario que se loggea
                .claim("userId", userId) //Pide el user id
                .claim("role", userRole) //Pide el role
                .setIssuedAt(now) //Pide la hora a la que se crea el token
                .setExpiration(tokenExpiration) //Pide la hora de expiración, que es 1 hora después
                .signWith(secretKey, SignatureAlgorithm.HS512) //Pide la firma de seguridad
                .compact(); //Compacta la información y genera un token único
    }

    public String generateRefreshToken(String email, String userRole) {
        Date now = new Date();
        Date tokenRefreshExpiration = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim("role", userRole)
                .setIssuedAt(now)
                .setExpiration(tokenRefreshExpiration)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60 * 2000)
                .build();

        return refreshCookie.toString();
    }

    /**
     * Extrae todos los claims (información codificada) de un token JWT.
     *
     * @param token El token JWT a analizar.
     * @return Un objeto Claims con el contenido del token.
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Valida si el token JWT es válido:
     * - Que el email y el rol coincidan con los esperados.
     * - Que no haya expirado.
     *
     * @param token El token JWT a validar.
     * @param email Email del usuario autenticado.
     * @param role  Rol del usuario autenticado.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token, String email, String role) {
        final Claims claims = extractClaims(token);
        final String extractedEmail = claims.getSubject();
        final String extractedRole = claims.get("role", String.class);
        return (extractedEmail.equals(email) && extractedRole.equals(role) && !isTokenExpired(token));
    }

    /**
     * Verifica si el token ya expiró.
     *
     * @param token El token JWT a verificar.
     * @return true si el token ya no es válido por tiempo, false si aún está activo.
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
