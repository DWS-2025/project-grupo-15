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

    @GetMapping({"/login"})
    public String login(Model model) {
        if(loginAttemptService.isBlocked()){
            model.addAttribute("error", "Demasiados intentos fallidos. Inténtalo nuevamente en 10 minutos.");
            return "loginerror";
        }
        return "login";
    }

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

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO, String password) {
        userService.add(userDTO, password);
        return "saved_user";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/login-profile")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO());
        return "profile";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.remove(id);
        return "deleted_user";
    }

    @PostMapping("/login-profile/delete")
    public String deleteMyUser(HttpServletResponse response, HttpServletRequest request) {
        userService.remove(this.userService.getLoggedUserDTO().id());
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "deleted_user";
    }

    @GetMapping("/login-profile/edit")
    public String editMyUser(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO());
        return "edit_user";
    }

    @PostMapping("/login-profile/edit")
    public String editUser(UserDTO userDTO, String password) {
        userService.update(userDTO, password);
        return "saved_user";
    }
}