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

@Service
public class RepositoryUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public RepositoryUserDetailsService() {
    }

    /**
     * Loads user details by username.
     *
     * @param username the username of the user
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if the user is not found
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User)this.userRepository.findByName(username).orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });
        List<GrantedAuthority> roles = new ArrayList();
        Iterator var5 = user.getRoles().iterator();

        while(var5.hasNext()) {
            String role = (String)var5.next();
            roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getEncodedPassword(), roles);
    }
}