package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserControllerREST {
    @Autowired
    private UserService userService; // Service to handle user-related functionality

    /**
     * Handles the GET request to retrieve and display all users
     *
     * @return "users" view to display the list of users
     */
    @GetMapping("")
    public Collection<UserDTO> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/login-profile")
    public UserDTO me() {
        return this.userService.getLoggedUserDTO(); // Add all users to the model
    }

    @DeleteMapping("/login-profile")
    public UserDTO deleteMe(HttpServletResponse response, HttpServletRequest request) {
        UserDTO removedUser = this.userService.getLoggedUserDTO(); 
        userService.remove(removedUser.id());
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return removedUser; // Return the "profile" view to render the of user page
    }

    @PutMapping("/login-profile")
    public UserDTO updateMe(UserDTO userDTO, String password) {
        userService.update(userDTO, password);
        return userDTO;
    }

    @DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable long id) {
        UserDTO removedUser = userService.findById(id);
        userService.remove(removedUser.id());
        return removedUser;
    }
}
