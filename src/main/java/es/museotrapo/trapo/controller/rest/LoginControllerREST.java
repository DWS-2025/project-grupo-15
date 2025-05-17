package es.museotrapo.trapo.controller.rest;

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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return userLogingService.login(response, loginRequest);
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
