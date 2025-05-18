package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Handles the GET request to retrieve a specific user by ID
     *
     * @param id the ID of the user to retrieve
     * @return the UserDTO object representing the user with the specified ID
     */
    @GetMapping("/login-profile")
    public UserDTO me() {
        return this.userService.getLoggedUserDTO(); // Add all users to the model
    }

    /**
     * Handles the GET request to retrieve a specific user by ID
     *
     * @param id the ID of the user to retrieve
     * @return the UserDTO object representing the user with the specified ID
     */
    @DeleteMapping("/login-profile")
    public UserDTO deleteMe(HttpServletResponse response, HttpServletRequest request) {
        UserDTO removedUser = this.userService.getLoggedUserDTO(); 
        userService.remove(removedUser.id());
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return removedUser; // Return the "profile" view to render the of user page
    }

    /**
     * Handles the POST request to create a new user
     *
     * @param userDTO the UserDTO object representing the new user to create
     * @return the created UserDTO object
     */
    @PutMapping("/login-profile")
    public UserDTO updateMe(@RequestBody UserDTO userDTO) {
        return userService.update(userDTO, userDTO.encodedPassword());
    }

    /**
     * Handles the POST request to create a new user
     *
     * @param userDTO the UserDTO object representing the new user to create
     * @return the created UserDTO object
     */
    @DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable long id) {
        UserDTO removedUser = userService.findById(id);
        userService.remove(removedUser.id());
        return removedUser;
    }
}
