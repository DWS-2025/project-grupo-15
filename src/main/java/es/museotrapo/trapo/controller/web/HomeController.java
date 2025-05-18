package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Maps the root URL ("/") to the "index" view.
     *
     * @return The name of the view (index.html)
     */
    @GetMapping("/")
    public String principal() {
        return "index"; // Returns the "index" view (index.html)
    }
}