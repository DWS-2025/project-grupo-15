package es.museotrapo.trapo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.SecretKeyBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JWT (JSON Web Token) creation, validation, and parsing.
 * This component includes methods for generating, signing, and validating JWT tokens.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret = SIG.HS256.key().build(); // Secret key for signing JWTs
    private final JwtParser jwtParser = Jwts.parser().verifyWith(jwtSecret).build(); // JWT parser and validator

    /**
     * Extracts the JWT token from the `Authorization` header of an HTTP request.
     *
     * @param req The HTTP request containing the Authorization header.
     * @return The token string without the "Bearer " prefix.
     * @throws IllegalArgumentException If the header is missing or improperly formatted.
     */
    public String tokenStringFromHeaders(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null) {
            throw new IllegalArgumentException("Missing Authorization header");
        } else if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header does not start with Bearer: " + bearerToken);
        } else {
            return bearerToken.substring(7); // Remove the "Bearer " prefix
        }
    }

    /**
     * Extracts the JWT token from cookies in an HTTP request.
     *
     * @param request The HTTP request containing cookies.
     * @return The JWT token stored in a specific cookie.
     * @throws IllegalArgumentException If no cookies or relevant cookie is found.
     */
    private String tokenStringFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("No cookies found in request");
        } else {
            for (Cookie cookie : cookies) {
                if (TokenType.ACCESS.cookieName.equals(cookie.getName())) { // Identify the access token cookie
                    String accessToken = cookie.getValue();
                    if (accessToken == null) {
                        throw new IllegalArgumentException("Cookie %s has null value".formatted(TokenType.ACCESS.cookieName));
                    }
                    return accessToken;
                }
            }
            throw new IllegalArgumentException("No access token cookie found in request");
        }
    }

    /**
     * Validates a JWT token from either cookies or the Authorization header.
     *
     * @param req        The HTTP request containing the token.
     * @param fromCookie True if the token should be retrieved from cookies, false otherwise.
     * @return JWT claims extracted from the token.
     */
    public Claims validateToken(HttpServletRequest req, boolean fromCookie) {
        // Retrieve the token from cookies or headers
        String token = fromCookie ? this.tokenStringFromCookies(req) : this.tokenStringFromHeaders(req);
        return this.validateToken(token);
    }

    /**
     * Validates and parses the claims from a provided JWT token string.
     *
     * @param token The JWT token to validate.
     * @return The extracted claims from the token.
     */
    public Claims validateToken(String token) {
        return (Claims) this.jwtParser.parseSignedClaims(token).getPayload();
    }

    /**
     * Generates an access token for the given user details.
     *
     * @param userDetails The user's details (username, authorities, etc.).
     * @return A signed JWT access token.
     */
    public String generateAccessToken(UserDetails userDetails) {
        return this.buildToken(TokenType.ACCESS, userDetails).compact();
    }

    /**
     * Generates a refresh token for the given user details.
     *
     * @param userDetails The user's details (username, authorities, etc.).
     * @return A signed JWT refresh token.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return this.buildToken(TokenType.REFRESH, userDetails).compact();
    }

    /**
     * Helper method to build a JWT token using the specified token type and user details.
     *
     * @param tokenType   The type of token (ACCESS or REFRESH).
     * @param userDetails The user's details (username, roles, etc.).
     * @return A JwtBuilder object ready for signing and generation.
     */
    private JwtBuilder buildToken(TokenType tokenType, UserDetails userDetails) {
        Date currentDate = new Date(); // Token creation time
        Date expiryDate = Date.from(currentDate.toInstant().plus(tokenType.duration)); // Token expiration time
        return Jwts.builder()
                .claim("roles", userDetails.getAuthorities()) // Add user roles to the token claims
                .claim("type", tokenType.name()) // Indicate the type of token
                .subject(userDetails.getUsername()) // Set the token's subject as the user's username
                .issuedAt(currentDate) // Set the token's issued time
                .expiration(expiryDate) // Set the token's expiration time
                .signWith(this.jwtSecret); // Sign the token with the secret key
    }
}