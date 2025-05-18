package es.museotrapo.trapo.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This class acts as the entry point for handling unauthorized access attempts in the application.
 * It is invoked whenever a user tries to access a secured resource without proper authentication.
 */
@Component
public class UnauthorizedHandlerJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandlerJwt.class);

    /**
     * Default constructor.
     */
    public UnauthorizedHandlerJwt() {
    }

    /**
     * Handles unauthorized HTTP requests by sending a 401 (Unauthorized) error response.
     * This method is automatically triggered during authentication failures.
     *
     * @param request       The HTTP request that triggered the unauthorized access.
     * @param response      The HTTP response to be sent to the client.
     * @param authException The exception that occurred during authentication.
     * @throws IOException If there is an error while writing the response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // Log the unauthorized error with the exception message.
        logger.info("Unauthorized error: {}", authException.getMessage());

        // Send a 401 HTTP status code along with an error message in the response body.
        response.sendError(401, "message: %s, path: %s".formatted(authException.getMessage(), request.getServletPath()));
    }
}