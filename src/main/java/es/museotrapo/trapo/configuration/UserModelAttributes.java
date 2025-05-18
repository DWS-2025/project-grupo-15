package es.museotrapo.trapo.configuration;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * This class is responsible for adding user authentication information to the model for all web controllers.
 * It checks if the user is authenticated and adds relevant attributes to the model.
 */
@ControllerAdvice(
        basePackages = {"es.museotrapo.trapo.controller.web"}
)

public class UserModelAttributes {
    public UserModelAttributes() {
    }

    /**
     * Adds user authentication information to the model for all web controllers.
     *
     * @param model   The model to add attributes to.
     * @param request The HTTP request containing user authentication information.
     */
    @ModelAttribute
    public void modelAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if(principal != null) {
            model.addAttribute("logged", true);
            model.addAttribute("username", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
        }else{
            model.addAttribute("logged", false);
        }
    }
}