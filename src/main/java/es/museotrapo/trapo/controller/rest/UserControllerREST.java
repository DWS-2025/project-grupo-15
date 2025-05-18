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

/**
 * REST controller for managing user-related operations.
 * It provides endpoints for CRUD operations, as well as authentication-related actions
 * such as profile management and account deletion.
 */
@RestController
@RequestMapping("/api/users")
public class UserControllerREST {

    // Service to handle user-related business logic
    @Autowired
    private UserService userService;

    /**
     * Retrieves all users in the system.
     *
     * @return a collection of UserDTO objects representing all users.
     */
    @GetMapping("")
    public Collection<UserDTO> getUsers() {
        return userService.findAll(); // Fetch and return all users
    }

    /**
     * Retrieves information about the currently logged-in user.
     *
     * @return a UserDTO object representing the logged-in user's profile.
     */
    @GetMapping("/login-profile")
    public UserDTO me() {
        return this.userService.getLoggedUserDTO(); // Return the currently logged-in user's data
    }

    /**
     * Deletes the currently logged-in user's profile.
     * This action removes the user from the system and logs them out of the session.
     *
     * @param response the HTTP response used to clear the authentication cookies.
     * @param request  the HTTP request used to process the logout.
     * @return a UserDTO object representing the deleted user's information.
     */
    @DeleteMapping("/login-profile")
    public UserDTO deleteMe(HttpServletResponse response, HttpServletRequest request) {
        // Retrieve the logged-in user's information
        UserDTO removedUser = this.userService.getLoggedUserDTO();

        // Remove the user's account from the system
        userService.remove(removedUser.id());

        // Perform logout and clear the security context
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Return the deleted user's profile
        return removedUser;
    }

    /**
     * Updates the currently logged-in user's profile information.
     *
     * @param userDTO the updated user data from the client.
     * @return the updated UserDTO object with the new values.
     */
    @PutMapping("/login-profile")
    public UserDTO updateMe(@RequestBody UserDTO userDTO) {
        // Update the user's data and return the updated UserDTO
        return userService.update(userDTO, userDTO.encodedPassword());
    }

    /**
     * Deletes a specific user by their unique ID.
     *
     * @param id the ID of the user to delete.
     * @return a UserDTO object representing the deleted user's information.
     */
    @DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable long id) {
        // Retrieve the user's information by ID
        UserDTO removedUser = userService.findById(id);

        // Remove the user from the system
        userService.remove(removedUser.id());

        // Return the deleted user's profile
        return removedUser;
    }
}