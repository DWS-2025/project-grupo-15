package es.museotrapo.trapo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.museotrapo.trapo.service.UsernameService;

@Controller
public class UserController {

    @Autowired
    private UsernameService usernameService; // Service to handle user-related functionality

    /**
     * Handles the GET request to retrieve and display all users
     *
     * @param model Model object to add attributes for the view
     * @return "users" view to display the list of users
     */
    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", usernameService.findAll()); // Add all users to the model
        return "users"; // Return the "users" view to render the list of users
    }
}