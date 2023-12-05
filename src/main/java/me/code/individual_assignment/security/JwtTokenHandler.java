package me.code.individual_assignment.security;

import me.code.individual_assignment.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;

/**
 * Component for handling JWT (JSON Web Token) generation, validation, and retrieval of claims.
 */
@Component
public class JwtTokenHandler {

    // The secret key used for signing the JWT
    private final Key key = Keys.hmacShaKeyFor((MySecureRandom.fetchSecureRandom()).getBytes());

    /**
     * Generates a JWT token for the specified user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(User user) {
        int id = user.getId();
        String username = user.getUsername();

        return Jwts.builder()
                .setSubject(Integer.toString(id))
                .claim("id", id)
                .claim("username", username)
                .signWith(key)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to be validated.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the user ID from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The user ID retrieved from the token.
     */
    public int getTokenId(String token) {
        return getTokenClaim(token, "id", Integer.class);
    }

    /**
     * Retrieves a specific claim from the provided JWT token.
     *
     * @param token      The JWT token.
     * @param type       The type of claim to retrieve.
     * @param returnType The class type of the claim.
     * @param <T>        The generic type of the claim.
     * @return The retrieved claim of the specified type.
     */
    public <T> T getTokenClaim(String token, String type, Class<T> returnType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(type, returnType);
    }
}
