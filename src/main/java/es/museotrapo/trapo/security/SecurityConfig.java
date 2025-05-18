package es.museotrapo.trapo.security;

import es.museotrapo.trapo.security.jwt.JwtRequestFilter;
import es.museotrapo.trapo.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the application.
 * It manages authentication and authorization rules, defines security filters, JWT handling, and more.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // Custom filter to validate JWT tokens

    @Autowired
    private RepositoryUserDetailsService userDetailsService; // Service for loading user details

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt; // Handles unauthorized access attempts

    /**
     * Configures the password encoder to encode plain-text passwords (BCrypt hashing).
     *
     * @return Password encoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the `AuthenticationManager` used for authentication processes.
     *
     * @param authConf Spring's authentication configuration.
     * @return AuthenticationManager bean.
     * @throws Exception If something goes wrong while fetching the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    /**
     * Configures the Data Access Object (DAO) authentication provider.
     * This provider uses the `UserDetailsService` to load user information and checks passwords with the configured `PasswordEncoder`.
     *
     * @return DAO Authentication Provider bean.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set the custom user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
        return authProvider;
    }

    /**
     * Custom security filter chain for `/api/**` endpoints.
     * Defines authentication and authorization rules for REST APIs.
     *
     * @param http HttpSecurity object for configuring security.
     * @return SecurityFilterChain bean for API endpoints.
     * @throws Exception If configuration fails.
     */
    @Bean
    @Order(1) // Higher priority chain for APIs
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Use custom authentication provider
        http.authenticationProvider(authenticationProvider());

        // Match requests under `/api/**` and configure exception handling
        http.securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        // Define authorization rules for API endpoints
        http.authorizeHttpRequests(authorize -> authorize
                // Authentication and authorization rules for `/api/auth/**`
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/refresh").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").hasRole("USER")

                // ARTIST ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/artists").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/artists/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/artists").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/artists/{id}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/artists/{id}").hasRole("ADMIN")

                // PICTURE ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/pictures").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pictures/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/pictures").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/pictures/{id}").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/pictures/{id}").hasRole("ADMIN")

                // USER ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/login-profile").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/users/login-profile").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/users/login-profile").hasRole("USER")
        );

        // Disable CSRF, basic authentication, and session creation for APIs
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT validation filter before Spring's username-password auth filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Another security filter chain for non-API/custom web endpoints (e.g., web pages).
     * Configures public and private access rules for different URLs.
     *
     * @param http HttpSecurity object for configuring security.
     * @return SecurityFilterChain bean for web endpoints.
     * @throws Exception If configuration fails.
     */
    @Bean
    @Order(2) // Lower priority chain for general web pages
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        // Use the custom authentication provider
        http.authenticationProvider(authenticationProvider());

        // Authorization settings for web resources
        http.authorizeHttpRequests(authorize -> authorize
                // Public access endpoints
                .requestMatchers("/").permitAll()
                .requestMatchers("/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/favicon.ico").permitAll()

                // Private access endpoints
                .requestMatchers("/users").hasRole("ADMIN")
                .requestMatchers("/login-profile").hasAnyRole("USER", "ADMIN")
        );

        // Enable CSRF protection and configure form login/logout for web access
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/loginerror")
                .defaultSuccessUrl("/")
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
        );

        return http.build();
    }
}