package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.security.jwt.UserLogingService;
import es.museotrapo.trapo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private final UserService userService;
    private final UserLogingService userLogingService;

    public LoginController(UserService userService, UserLogingService userLogingService) {
        this.userService = userService;
        this.userLogingService = userLogingService;
    }

    @GetMapping({"/login"})
    public String login() {
        return "login";
    }


    @GetMapping({"/loginerror"})
    public String loginerror() {
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
        model.addAttribute("users",userService.findAll());
        return "users";
    }

    @GetMapping("/login-profile")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO()); // Add all users to the model
        return "profile"; // Return the "profile" view to render the of user page
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
    public String editUser(UserDTO userDTO, String password){
        userService.update(userDTO, password);
        return "saved_user";
    }
}
