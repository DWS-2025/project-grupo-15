package es.museotrapo.trapo.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.museotrapo.trapo.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService; // Service to handle user-related functionality

    /**
     * Handles the GET request to retrieve and display all users
     *
     * @param model Model object to add attributes for the view
     * @return "users" view to display the list of users
     */
    @GetMapping("")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO()); // Add all users to the model
        return "profile"; // Return the "profile" view to render the of user page
    }
}