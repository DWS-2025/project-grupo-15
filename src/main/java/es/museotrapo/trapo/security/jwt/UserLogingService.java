package es.museotrapo.trapo.security.jwt;

import es.museotrapo.trapo.security.LoginAttemptService;
import es.museotrapo.trapo.security.jwt.AuthResponse.Status;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user authentication, token management (login, refresh, logout),
 * and security-related operations for JWT-based authentication.
 */
@Service
public class UserLogingService {

    private static final Logger log = LoggerFactory.getLogger(UserLogingService.class);

    private final AuthenticationManager authenticationManager; // Manages authentication of credentials
    private final UserDetailsService userDetailsService; // Fetches user details
    private final JwtTokenProvider jwtTokenProvider; // Handles JWT token creation, validation, and refreshing

    /**
     * Constructor for dependency injection.
     *
     * @param authenticationManager The authentication manager for validating credentials.
     * @param userDetailsService    The service to retrieve user data from the database.
     * @param jwtTokenProvider      Utility class for generating and managing JWT tokens.
     */
    public UserLogingService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Logs in the user by authenticating credentials, generating tokens, and setting them in cookies.
     *
     * @param response     The HTTP response object for sending cookies.
     * @param loginRequest The login request containing the username and password.
     * @return A ResponseEntity containing the authentication result and message.
     */
    public ResponseEntity<AuthResponse> login(HttpServletResponse response, LoginRequest loginRequest) {
        // Authenticate user credentials
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Set the authenticated user's details in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = loginRequest.getUsername();

        // Retrieve user details
        UserDetails user = this.userDetailsService.loadUserByUsername(username);

        // Generate access and refresh tokens
        String newAccessToken = this.jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = this.jwtTokenProvider.generateRefreshToken(user);

        // Add tokens as cookies in the response
        response.addCookie(this.buildTokenCookie(TokenType.ACCESS, newAccessToken));
        response.addCookie(this.buildTokenCookie(TokenType.REFRESH, newRefreshToken));

        // Response message for successful authentication
        AuthResponse loginResponse = new AuthResponse(Status.SUCCESS, "Auth successful. Tokens are created in the cookie.");
        return ResponseEntity.ok().body(loginResponse);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param response      The HTTP response object for sending the new token cookie.
     * @param refreshToken  The refresh token given by the client.
     * @return A ResponseEntity containing the authentication result and message.
     */
    public ResponseEntity<AuthResponse> refresh(HttpServletResponse response, String refreshToken) {
        try {
            // Validate the provided refresh token
            Claims claims = this.jwtTokenProvider.validateToken(refreshToken);

            // Retrieve user details from the subject in the token
            UserDetails user = this.userDetailsService.loadUserByUsername(claims.getSubject());

            // Generate a new access token
            String newAccessToken = this.jwtTokenProvider.generateAccessToken(user);

            // Add the new access token as a cookie
            response.addCookie(this.buildTokenCookie(TokenType.ACCESS, newAccessToken));

            // Response message for successful token refresh
            AuthResponse loginResponse = new AuthResponse(Status.SUCCESS, "Auth successful. Tokens are created in the cookie.");
            return ResponseEntity.ok().body(loginResponse);
        } catch (Exception e) {
            log.error("Error while processing refresh token", e);

            // Response message for token refresh failure
            AuthResponse loginResponse = new AuthResponse(Status.FAILURE, "Failure while processing refresh token");
            return ResponseEntity.ok().body(loginResponse);
        }
    }

    /**
     * Logs out the user by removing authentication information and clearing cookies.
     *
     * @param response The HTTP response object for clearing token cookies.
     * @return A logout success message.
     */
    public String logout(HttpServletResponse response) {
        // Clear the SecurityContext
        SecurityContextHolder.clearContext();

        // Remove token cookies
        response.addCookie(this.removeTokenCookie(TokenType.ACCESS));
        response.addCookie(this.removeTokenCookie(TokenType.REFRESH));

        return "Logout successfully";
    }

    /**
     * Helper method to create a secure cookie for a token.
     *
     * @param type  The type of token (ACCESS or REFRESH).
     * @param token The generated token string.
     * @return A Cookie object with the token and appropriate settings.
     */
    private Cookie buildTokenCookie(TokenType type, String token) {
        Cookie cookie = new Cookie(type.cookieName, token);
        cookie.setMaxAge((int) type.duration.getSeconds()); // Set cookie expiration based on token duration
        cookie.setHttpOnly(true); // Restrict cookie access to HTTP requests only
        cookie.setPath("/"); // Allow the cookie to be used in all application paths
        return cookie;
    }

    /**
     * Helper method to remove a token cookie by setting its expiration time to 0.
     *
     * @param type The type of token (ACCESS or REFRESH).
     * @return A Cookie object with the token cleared.
     */
    private Cookie removeTokenCookie(TokenType type) {
        Cookie cookie = new Cookie(type.cookieName, "");
        cookie.setMaxAge(0); // Expire the cookie immediately
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}