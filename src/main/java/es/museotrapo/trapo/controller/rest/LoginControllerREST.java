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

@RestController
@RequestMapping("/api/auth")
public class LoginControllerREST {

    @Autowired
    private UserLogingService userLogingService;

    @Autowired
    private UserService userService;
    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     * Endpoint for user login.
     * It handles the authentication process and returns an AuthResponse.
     *
     * @param loginRequest the login request containing username and password.
     * @param response     the HTTP response to set cookies.
     * @return AuthResponse indicating success or failure of the login attempt.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        // Validar si el usuario está bloqueado

        if (loginAttemptService.isBlocked()) {
            return ResponseEntity.status(429).body(
                    new AuthResponse(AuthResponse.Status.FAILURE, "Demasiados intentos fallidos. Usuario bloqueado temporalmente.")
            );
        }
        try {
            // Realizar la autenticación
            ResponseEntity<AuthResponse> authResponse = userLogingService.login(response, loginRequest);
            loginAttemptService.resetAttempts();
            return authResponse;

        } catch (Exception e) {
            // Incrementar intentos fallidos si la autenticación falla
            loginAttemptService.registerLoginFailure();
            return ResponseEntity.status(401).body(
                    new AuthResponse(AuthResponse.Status.FAILURE, "Credenciales inválidas.")
            );
        }
    }

    /**
     * Endpoint to refresh the authentication token.
     * It uses the refresh token stored in a cookie to generate a new access token.
     *
     * @param refreshToken the refresh token from the cookie.
     * @param response     the HTTP response to set cookies.
     * @return AuthResponse containing the new access token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return userLogingService.refresh(response, refreshToken);
    }

    /**
     * Endpoint to log out the user.
     * It invalidates the refresh token and clears the cookies.
     *
     * @param response the HTTP response to set cookies.
     * @return AuthResponse indicating success or failure of the logout attempt.
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userLogingService.logout(response)));
    }

    /**
     * Endpoint to register a new user.
     * It creates a new user with the provided UserDTO and encodes the password.
     *
     * @param userDTO the user data to be registered.
     * @return UserDTO of the registered user.
     */
    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        userService.add(userDTO,userDTO.encodedPassword());
        return userDTO;
    }

}
