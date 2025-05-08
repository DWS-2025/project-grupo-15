package es.museotrapo.trapo.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UnauthorizedHandlerJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandlerJwt.class);

    public UnauthorizedHandlerJwt() {
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.info("Unauthorized error: {}", authException.getMessage());
        response.sendError(401, "message: %s, path: %s".formatted(authException.getMessage(), request.getServletPath()));
    }
}