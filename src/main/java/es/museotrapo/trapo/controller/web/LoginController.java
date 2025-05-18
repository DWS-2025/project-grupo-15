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

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserLogingService userLogingService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     * Map to store the number of failed login attempts for each user.
     * The key is the username, and the value is the number of failed attempts.
     */
    @GetMapping({"/login"})
    public String login(Model model) {
        if(loginAttemptService.isBlocked()){
            model.addAttribute("error", "Demasiados intentos fallidos. Inténtalo nuevamente en 10 minutos.");
            return "loginerror";
        }
        return "login";
    }

    /**
     * Handles the login error and adds an error message to the model.
     *
     * @param request The HTTP request containing user authentication information.
     * @param model   The model to add attributes to.
     * @return The view name "loginerror".
     */
    @GetMapping("/loginerror")
    public String loginError(HttpServletRequest request, Model model) {

        if (loginAttemptService.isBlocked()) {
            model.addAttribute("error", "Demasiados intentos fallidos. Inténtalo nuevamente en 10 minutos.");
            return "loginerror";
        }

        loginAttemptService.registerLoginFailure(); // Registrar intento fallido

        model.addAttribute("error", "Credenciales inválidas.");
        return "loginerror";
    }

    /**
     * Handles the registration of a new user.
     *
     * @param userDTO The UserDTO object representing the new user to create.
     * @param password The password for the new user.
     * @return The view name "saved_user".
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * Handles the registration of a new user.
     *
     * @param userDTO The UserDTO object representing the new user to create.
     * @param password The password for the new user.
     * @return The view name "saved_user".
     */
    @PostMapping("/register")
    public String register(UserDTO userDTO, String password) {
        userService.add(userDTO, password);
        return "saved_user";
    }

    /**
     * Displays the list of all users.
     *
     * @param model The model to add attributes to.
     * @return The view name "users".
     */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    /**
     * Displays the form to edit a user based on the provided ID.
     *
     * @param model The model to add attributes to.
     * @param id    The ID of the user to edit.
     * @return The view name "edit_user".
     */
    @GetMapping("/login-profile")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO());
        return "profile";
    }

    /**
     * Displays the form to edit a user based on the provided ID.
     *
     * @param model The model to add attributes to.
     * @param id    The ID of the user to edit.
     * @return The view name "edit_user".
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.remove(id);
        return "deleted_user";
    }

    /**
     * Handles the deletion of the logged-in user.
     *
     * @param response The HTTP response to set cookies.
     * @param request  The HTTP request to log out the user.
     * @return The view name "deleted_user".
     */
    @PostMapping("/login-profile/delete")
    public String deleteMyUser(HttpServletResponse response, HttpServletRequest request) {
        userService.remove(this.userService.getLoggedUserDTO().id());
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "deleted_user";
    }

    /**
     * Displays the form to edit a user based on the provided ID.
     *
     * @param model The model to add attributes to.
     * @param id    The ID of the user to edit.
     * @return The view name "edit_user".
     */
    @GetMapping("/login-profile/edit")
    public String editMyUser(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO());
        return "edit_user";
    }

    /**
     * Handles the update of a user.
     *
     * @param userDTO  The UserDTO object representing the updated user.
     * @param password The new password for the user.
     * @return The view name "saved_user".
     */
    @PostMapping("/login-profile/edit")
    public String editUser(UserDTO userDTO, String password) {
        userService.update(userDTO, password);
        return "saved_user";
    }
}