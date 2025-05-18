package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling requests to the home or root URL.
 * Responsible for mapping the root URL ("/") to the main index page.
 */
@Controller
public class HomeController {

    // Service for handling user-related functionality
    private final UserService userService;

    /**
     * Constructor-based dependency injection for UserService.
     *
     * @param userService The service used for user-related operations.
     */
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Maps the root URL ("/") to the "index" view.
     * This serves as the main home page of the application.
     *
     * @return The name of the view ("index.html").
     */
    @GetMapping("/")
    public String principal() {
        return "index"; // Returns the "index" view for rendering the main page
    }
}