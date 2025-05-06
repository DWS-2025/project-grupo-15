package es.museotrapo.trapo.configuration;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
//COMPRUEBA PARA TODOS LOS LOS MODEL DEVUELTOS DEL CONTROLLER WEB SI EL USUARIO ESTA AUTENTICADO
@ControllerAdvice(
        basePackages = {"es.museotrapo.trapo.controller.web"}
)

public class UserModelAttributes {
    public UserModelAttributes() {
    }

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