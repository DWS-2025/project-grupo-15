package es.museotrapo.trapo.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation of Spring Security's {@link UserDetailsService}.
 * This class integrates a custom `User` entity and database to provide user authentication details.
 */
@Service
public class RepositoryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Injected repository for accessing the user database

    /**
     * Default constructor.
     */
    public RepositoryUserDetailsService() {
    }

    /**
     * Loads a user's details from the database by their username.
     *
     * @param username The username of the user attempting to authenticate.
     * @return A {@link UserDetails} object representing the authenticated user.
     * @throws UsernameNotFoundException If the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user from the repository by their username
        User user = this.userRepository.findByName(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );

        // Convert the user's roles into Spring Security's `GrantedAuthority` objects
        List<GrantedAuthority> roles = new ArrayList<>();
        Iterator<String> roleIterator = user.getRoles().iterator();

        while (roleIterator.hasNext()) {
            String role = roleIterator.next();
            roles.add(new SimpleGrantedAuthority("ROLE_" + role)); // Prefix roles with "ROLE_" (Spring convention)
        }

        // Create and return a Spring Security User object with the fetched information
        return new org.springframework.security.core.userdetails.User(
                user.getName(),            // Username
                user.getEncodedPassword(), // Encoded password (e.g., hashed with bcrypt)
                roles                      // Granted authorities (roles)
        );
    }
}