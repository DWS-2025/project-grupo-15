package es.museotrapo.trapo.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter processes incoming HTTP requests to extract and validate a JWT token.
 * It runs **once per request** to handle authentication-related tasks and sets the
 * authenticated user's details in the SecurityContext.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final UserDetailsService userDetailsService; // Service to fetch UserDetails from database
    private final JwtTokenProvider jwtTokenProvider; // Helper class to validate and parse JWT tokens

    /**
     * Constructor for dependency injection of UserDetailsService and JwtTokenProvider.
     *
     * @param userDetailsService The service to fetch user-specific details.
     * @param jwtTokenProvider  The utility class to handle JWT tokens (validation and parsing).
     */
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Filters the incoming request, validates the JWT, and sets the user's authentication details
     * to the SecurityContext.
     *
     * @param request     The HTTP request object.
     * @param response    The HTTP response object.
     * @param filterChain The filter chain allowing the next filters to be processed.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Validate and parse the JWT token from the request. Extract claims.
            var claims = this.jwtTokenProvider.validateToken(request, true);

            // Load user details from the username extracted from JWT claims.
            var userDetails = this.userDetailsService.loadUserByUsername(claims.getSubject());

            // Create an authenticated object for the request.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // Add additional details about the request source (IP address, headers, etc.).
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authenticated user object into the SecurityContext.
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {
            // Log an exception if there is any issue with token processing, except for missing access tokens.
            if (!ex.getMessage().equals("No access token cookie found in request")) {
                log.debug("Exception processing JWT Token: ", ex);
            }
        }

        // Continue with the filter chain to process the request.
        filterChain.doFilter(request, response);
    }
}