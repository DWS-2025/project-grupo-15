package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserControllerREST {
    @Autowired
    private UserService userService; // Service to handle user-related functionality

    /**
     * Handles the GET request to retrieve and display all users
     *
     * @return "users" view to display the list of users
     */
    @GetMapping("")
    public Collection<UserDTO> getUsers() {
        return userService.findAll();
    }
}
