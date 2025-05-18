package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.security.LoginAttemptService;
import es.museotrapo.trapo.security.jwt.UserLogingService;
import es.museotrapo.trapo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for managing login, registration, and user-related actions.
 * Handles login attempts, user management functionalities, and security-related processes.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService; // Service for handling user-related operations

    @Autowired
    private UserLogingService userLogingService; // Service for managing user login using JWT

    @Autowired
    private LoginAttemptService loginAttemptService; // Service for monitoring login attempts and blocking on excessive failures

    /**
     * Displays the login page.
     * If the user is blocked due to excessive failed login attempts, an error message is shown.
     *
     * @param model The model to add error messages if applicable.
     * @return The login view or the error view on failure.
     */
    @GetMapping({"/login"})
    public String login(Model model) {
        if (loginAttemptService.isBlocked()) {
            model.addAttribute("error", "Too many failed attempts. Please try again in 10 minutes.");
            return "loginerror";
        }
        return "login"; // Return the login page
    }

    /**
     * Handles login errors by displaying an error message.
     * Also registers failed login attempts and blocks future attempts if necessary.
     *
     * @param request The HTTP request object.
     * @param model   The model to add error messages.
     * @return The view for login errors with the appropriate error message.
     */
    @GetMapping("/loginerror")
    public String loginError(HttpServletRequest request, Model model) {
        if (loginAttemptService.isBlocked()) {
            model.addAttribute("error", "Too many failed attempts. Please try again in 10 minutes.");
            return "loginerror";
        }

        loginAttemptService.registerLoginFailure(); // Register the failed attempt
        model.addAttribute("error", "Invalid credentials."); // Pass error message to the view
        return "loginerror";
    }

    /**
     * Displays the registration page where new users can sign up.
     *
     * @return The view name "register".
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * Processes the registration of a new user.
     *
     * @param userDTO  The user details submitted via the registration form.
     * @param password The password chosen by the user.
     * @return The view "saved_user" after successful registration.
     */
    @PostMapping("/register")
    public String register(UserDTO userDTO, String password) {
        userService.add(userDTO, password); // Add the user to the system
        return "saved_user"; // Redirect to the confirmation page
    }

    /**
     * Displays a list of all registered users.
     *
     * @param model The model to add the user list.
     * @return The "users" view displaying all users.
     */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll()); // Fetch all users and add to the model
        return "users";
    }

    /**
     * Displays the profile of the currently logged-in user.
     *
     * @param model The model to add the logged-in user's details.
     * @return The "profile" view displaying the user's profile information.
     */
    @GetMapping("/login-profile")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO()); // Fetch logged-in user's details
        return "profile";
    }

    /**
     * Deletes a specific user by their ID.
     *
     * @param id The ID of the user to be deleted.
     * @return The view "deleted_user" after successful deletion.
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.remove(id); // Delete the user with the specified ID
        return "deleted_user";
    }

    /**
     * Deletes the currently logged-in user's account and logs them out.
     *
     * @param response The HTTP response used to clear the authentication cookies.
     * @param request  The HTTP request used to access the current authenticated user.
     * @return The view "deleted_user" after successful deletion and logout.
     */
    @PostMapping("/login-profile/delete")
    public String deleteMyUser(HttpServletResponse response, HttpServletRequest request) {
        // Delete the logged-in user's account
        userService.remove(this.userService.getLoggedUserDTO().id());

        // Perform user logout
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "deleted_user"; // Confirmation screen after deletion
    }

    /**
     * Displays the form to edit the currently logged-in user's account.
     *
     * @param model The model to add the logged-in user's details to populate the form.
     * @return The "edit_user" view.
     */
    @GetMapping("/login-profile/edit")
    public String editMyUser(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO()); // Fetch logged-in user's profile
        return "edit_user"; // Display the form with user details
    }

    /**
     * Processes the form submission to update the user's account information.
     *
     * @param userDTO  The updated user details.
     * @param password The new password for the user.
     * @return The view "saved_user" after successful update.
     */
    @PostMapping("/login-profile/edit")
    public String editUser(UserDTO userDTO, String password) {
        userService.update(userDTO, password); // Update user information
        return "saved_user"; // Confirmation screen
    }
}