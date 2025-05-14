package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;
import org.springframework.stereotype.Controller;
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
}
