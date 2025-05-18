package es.museotrapo.trapo.configuration;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

// This class adds attributes to the model for all web controllers in the specified basePackages
@ControllerAdvice(
        basePackages = {"es.museotrapo.trapo.controller.web"}
)
public class UserModelAttributes {

    public UserModelAttributes() {
    }

    // This method checks if the user is authenticated and adds related attributes to the model
    @ModelAttribute
    public void modelAttributes(Model model, HttpServletRequest request) {
        // Retrieve the user's principal (authentication data)
        Principal principal = request.getUserPrincipal();

        if (principal != null) { // If the user is authenticated
            model.addAttribute("logged", true); // Add attribute indicating the user is logged in
            model.addAttribute("username", principal.getName()); // Add the username to the model
            model.addAttribute("admin", request.isUserInRole("ADMIN")); // Check and add if the user is an admin
        } else { // If the user is not authenticated
            model.addAttribute("logged", false); // Indicate the user is not logged in
        }
    }
}