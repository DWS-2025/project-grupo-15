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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    RepositoryUserDetailsService userDetailsService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService((UserDetailsService) userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.securityMatcher("/api/**").
                exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http.authorizeHttpRequests(authorize -> authorize
                // LoginController
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
                .requestMatchers(HttpMethod.GET, "/api/pictures/{id}/image").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pictures/{id}/comments").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/pictures").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/pictures/{id}/comments").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/pictures/{id}/likes").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/pictures/{id}/image").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/pictures/{id}/image").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/pictures/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/pictures/{id}/comments/{commentId}").hasRole("USER")

                // UserController
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/login-profile").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/users/login-profile").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/users/login-profile").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/artists/{id}/biography").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/artists/{id}/biography").hasRole("USER")

                
        );
        //Disable form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        //Disable CSRF protection
        http.csrf(csrf -> csrf.disable());

        //Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        //Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //Add JWT token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(authorize -> authorize
                //PUBLIC
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/cuadro.jpg").permitAll()
                                .requestMatchers("/pictures/{id}/{imageFile}").permitAll()
                                .requestMatchers("/pictures/{id}").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/artists/{id}").permitAll()
                                .requestMatchers("/js/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/milogo.png").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/favicon.ico").permitAll()
                                .requestMatchers("/pictures").permitAll()
                                .requestMatchers("/artists").permitAll()
                                .requestMatchers("/artists/more").permitAll()
                //PRIVATE
                                .requestMatchers("/users").hasAnyRole("ADMIN")
                                .requestMatchers("/users/{id}/delete").hasAnyRole("ADMIN")
                                .requestMatchers("/login-profile").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/login-profile/delete").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/login-profile/edit").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/pictures/new").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/artists/new").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/artists/{id}/edit").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/artists/{id}/delete").hasAnyRole("ADMIN")
                                .requestMatchers("artists/{id}/biography").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/pictures/{id}/delete").hasAnyRole("ADMIN")
                                .requestMatchers("/pictures/{id}/likeToggle").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/pictures/{id}/comments/new").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/pictures/{id}/comments/{commentId}/delete").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/logout").hasAnyRole("ADMIN", "USER")

                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        //http.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/error")));
        return http.build();
    }
}
