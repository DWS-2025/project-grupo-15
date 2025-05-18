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


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return userLogingService.refresh(response, refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userLogingService.logout(response)));
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        userService.add(userDTO,userDTO.encodedPassword());
        return userDTO;
    }

}
