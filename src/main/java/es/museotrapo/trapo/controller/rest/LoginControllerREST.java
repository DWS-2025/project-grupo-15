package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.security.LoginAttemptService;
import es.museotrapo.trapo.security.jwt.AuthResponse;
import es.museotrapo.trapo.security.jwt.LoginRequest;
import es.museotrapo.trapo.security.jwt.UserLogingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;

/**
 * REST controller for handling authentication operations such as login,
 * registration, logout, and token refresh.
 */
@RestController
@RequestMapping("/api/auth")
public class LoginControllerREST {

    // Service responsible for handling user login operations
    @Autowired
    private UserLogingService userLogingService;

    // Service for managing user data and registration
    @Autowired
    private UserService userService;

    // Service responsible for managing login attempt tracking and blocking logic
    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     * Login endpoint.
     * Authenticates a user with the provided credentials and manages login attempts.
     *
     * @param loginRequest the login request containing username and password.
     * @param response     the HTTP response object to set authentication cookies.
     * @return a ResponseEntity containing an AuthResponse (success or failure).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        // Check if the user is temporarily blocked from logging in
        if (loginAttemptService.isBlocked()) {
            return ResponseEntity.status(429).body( // Too Many Requests (HTTP 429)
                    new AuthResponse(AuthResponse.Status.FAILURE, "Too many failed attempts. User temporarily blocked.")
            );
        }
        try {
            // Attempt user authentication
            ResponseEntity<AuthResponse> authResponse = userLogingService.login(response, loginRequest);
            loginAttemptService.resetAttempts(); // Reset login attempts on successful login
            return authResponse;

        } catch (Exception e) {
            // Increment failed login attempts if authentication fails
            loginAttemptService.registerLoginFailure();
            return ResponseEntity.status(401).body( // Unauthorized (HTTP 401)
                    new AuthResponse(AuthResponse.Status.FAILURE, "Invalid credentials.")
            );
        }
    }

    /**
     * Token refresh endpoint.
     * Refreshes the authentication token using the refresh token stored in cookies.
     *
     * @param refreshToken the refresh token from the browser cookie.
     * @param response     the HTTP response object to update cookies.
     * @return a ResponseEntity containing an updated AuthResponse.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return userLogingService.refresh(response, refreshToken);
    }

    /**
     * Logout endpoint.
     * Logs the user out by clearing authentication-related cookies.
     *
     * @param response the HTTP response object to clear cookies.
     * @return a ResponseEntity with a success AuthResponse.
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userLogingService.logout(response)));
    }

    /**
     * Registration endpoint.
     * Registers a new user with the provided data and encoded password.
     *
     * @param userDTO the user data (username, email, password, etc.).
     * @return the UserDTO of the newly registered user.
     */
    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        userService.add(userDTO, userDTO.encodedPassword()); // Save the user with an encoded password
        return userDTO;
    }
}
