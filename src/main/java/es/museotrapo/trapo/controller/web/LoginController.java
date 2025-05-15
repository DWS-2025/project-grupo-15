package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
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
    public String users() {
        return "users";
    }
    @GetMapping("/login-profile")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO()); // Add all users to the model
        return "profile"; // Return the "profile" view to render the of user page
    }
}
