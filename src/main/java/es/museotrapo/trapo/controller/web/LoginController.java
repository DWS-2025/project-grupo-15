package es.museotrapo.trapo.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    public LoginController() {}

    @GetMapping({"/login"})
    public String login() {
        return "login";
    }

    @GetMapping({"/loginerror"})
    public String loginerror() {
        return "loginerror";
    }
}
