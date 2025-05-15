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
                //PRIVATE ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/api/users/me").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/pictures/").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/api/artists/").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "api/artists/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "api/pictures/**").hasRole("ADMIN")
                //PUBLIC ENDPOINTS
                .anyRequest().permitAll()
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
                                .requestMatchers("/").permitAll().requestMatchers("/cuadro.jpg").permitAll()
                                .requestMatchers("/pictures/**").permitAll().requestMatchers("/css/**").permitAll()
                                .requestMatchers("/artists/**").permitAll().requestMatchers("/js/**").permitAll()
                                .requestMatchers("/error").permitAll().requestMatchers("/milogo.png").permitAll()
                                .requestMatchers("/register").permitAll().requestMatchers("/favicon.ico").permitAll()
                //PRIVATE
                        .requestMatchers("/users/").hasAnyRole("ADMIN")
                        .requestMatchers("/login-profile").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/pictures/new").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/artists/new").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/artists/edit").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/pictures/*/delete").hasAnyRole("ADMIN")
                        .requestMatchers("/artists/delete").hasAnyRole("ADMIN")
                        .requestMatchers("/pictures/*/likeToggle").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/pictures/*/comments/new").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/pictures/*/comments/*/delete").hasAnyRole("ADMIN")
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
        return http.build();
    }
}
