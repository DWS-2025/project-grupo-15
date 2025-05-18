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

@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret = SIG.HS256.key().build();
    private final JwtParser jwtParser = Jwts.parser().verifyWith(jwtSecret).build();

    /**
     * Enum representing the type of token (ACCESS or REFRESH) and its duration.
     */
    public String tokenStringFromHeaders(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null) {
            throw new IllegalArgumentException("Missing Authorization header");
        } else if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header does not start with Bearer: " + bearerToken);
        } else {
            return bearerToken.substring(7);
        }
    }

    /**
     * Extracts the access token from the cookies in the request.
     *
     * @param request The HTTP request containing the cookies
     * @return The access token string
     */
    private String tokenStringFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("No cookies found in request");
        } else {
            Cookie[] var6 = cookies;
            int var5 = cookies.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                Cookie cookie = var6[var4];
                if (TokenType.ACCESS.cookieName.equals(cookie.getName())) {
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
     * Validates the JWT token and extracts the claims.
     *
     * @param req The HTTP request containing the JWT token
     * @param fromCookie Indicates whether to extract the token from cookies or headers
     * @return The claims extracted from the token
     */
    public Claims validateToken(HttpServletRequest req, boolean fromCookie) {
        String token = fromCookie ? this.tokenStringFromCookies(req) : this.tokenStringFromHeaders(req);
        return this.validateToken(token);
    }

    /**
     * Validates the JWT token and extracts the claims.
     *
     * @param token The JWT token string
     * @return The claims extracted from the token
     */
    public Claims validateToken(String token) {
        return (Claims)this.jwtParser.parseSignedClaims(token).getPayload();
    }

    /**
     * Enum representing the type of token (ACCESS or REFRESH) and its duration.
     */
    public String generateAccessToken(UserDetails userDetails) {
        return this.buildToken(TokenType.ACCESS, userDetails).compact();
    }

    /**
     * Enum representing the type of token (ACCESS or REFRESH) and its duration.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        JwtBuilder token = this.buildToken(TokenType.REFRESH, userDetails);
        return token.compact();
    }

    /**
     * Enum representing the type of token (ACCESS or REFRESH) and its duration.
     */
    private JwtBuilder buildToken(TokenType tokenType, UserDetails userDetails) {
        Date currentDate = new Date();
        Date expiryDate = Date.from((new Date()).toInstant().plus(tokenType.duration));
        return Jwts.builder().claim("roles", userDetails.getAuthorities()).claim("type", tokenType.name()).subject(userDetails.getUsername()).issuedAt(currentDate).expiration(expiryDate).signWith(this.jwtSecret);
    }
}

