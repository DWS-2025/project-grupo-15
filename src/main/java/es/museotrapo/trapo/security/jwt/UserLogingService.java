package es.museotrapo.trapo.security.jwt;

import es.museotrapo.trapo.security.LoginAttemptService;
import es.museotrapo.trapo.security.jwt.AuthResponse.Status;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserLogingService {
    private static final Logger log = LoggerFactory.getLogger(UserLogingService.class);

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor for UserLogingService.
     *
     * @param authenticationManager the AuthenticationManager to authenticate users
     * @param userDetailsService    the UserDetailsService to load user details
     * @param jwtTokenProvider      the JwtTokenProvider to generate and validate JWT tokens
     */
    public UserLogingService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticates a user and generates JWT tokens.
     *
     * @param response      the HTTP response to set the cookies
     * @param loginRequest  the login request containing username and password
     * @return a ResponseEntity containing the authentication response
     */
    public ResponseEntity<AuthResponse> login(HttpServletResponse response, LoginRequest loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = loginRequest.getUsername();
        UserDetails user = this.userDetailsService.loadUserByUsername(username);
        HttpHeaders responseHeaders = new HttpHeaders();
        String newAccessToken = this.jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = this.jwtTokenProvider.generateRefreshToken(user);
        response.addCookie(this.buildTokenCookie(TokenType.ACCESS, newAccessToken));
        response.addCookie(this.buildTokenCookie(TokenType.REFRESH, newRefreshToken));
        AuthResponse loginResponse = new AuthResponse(Status.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().headers(responseHeaders)).body(loginResponse);
    }

    /**
     * Refreshes the access token using the refresh token.
     *
     * @param response      the HTTP response to set the new access token cookie
     * @param refreshToken  the refresh token to validate and generate a new access token
     * @return a ResponseEntity containing the authentication response
     */
    public ResponseEntity<AuthResponse> refresh(HttpServletResponse response, String refreshToken) {
        try {
            Claims claims = this.jwtTokenProvider.validateToken(refreshToken);
            UserDetails user = this.userDetailsService.loadUserByUsername(claims.getSubject());
            String newAccessToken = this.jwtTokenProvider.generateAccessToken(user);
            response.addCookie(this.buildTokenCookie(TokenType.ACCESS, newAccessToken));
            AuthResponse loginResponse = new AuthResponse(Status.SUCCESS, "Auth successful. Tokens are created in cookie.");
            return ResponseEntity.ok().body(loginResponse);
        } catch (Exception var7) {
            log.error("Error while processing refresh token", var7);
            AuthResponse loginResponse = new AuthResponse(Status.FAILURE, "Failure while processing refresh token");
            return ResponseEntity.ok().body(loginResponse);
        }
    }

    /**
     * Logs out the user by clearing the security context and removing the cookies.
     *
     * @param response the HTTP response to set the cookies
     * @return a message indicating successful logout
     */
    public String logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        response.addCookie(this.removeTokenCookie(TokenType.ACCESS));
        response.addCookie(this.removeTokenCookie(TokenType.REFRESH));
        return "logout successfully";
    }

    /**
     * Handles login attempts and tracks failed attempts.
     *
     * @param loginRequest the login request containing username and password
     * @return a ResponseEntity containing the authentication response
     */
    private Cookie buildTokenCookie(TokenType type, String token) {
        Cookie cookie = new Cookie(type.cookieName, token);
        cookie.setMaxAge((int) type.duration.getSeconds());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Removes the token cookie by setting its max age to 0.
     *
     * @param type the type of token to remove (ACCESS or REFRESH)
     * @return the removed cookie
     */
    private Cookie removeTokenCookie(TokenType type) {
        Cookie cookie = new Cookie(type.cookieName, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
