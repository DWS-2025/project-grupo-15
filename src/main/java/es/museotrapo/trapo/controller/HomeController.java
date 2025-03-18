package es.museotrapo.trapo.controller;

import es.museotrapo.trapo.service.UsernameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UsernameService usernameService;

    public HomeController(UsernameService usernameService) {
        this.usernameService = usernameService;
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