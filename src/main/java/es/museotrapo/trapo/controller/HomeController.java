package es.museotrapo.trapo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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