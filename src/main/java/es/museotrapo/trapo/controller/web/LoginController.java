package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.UserDTO;
import es.museotrapo.trapo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/login"})
    public String login() {
        return "login";
    }


    @GetMapping({"/loginerror"})
    public String loginerror() {
        return "loginerror";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDTO userDTO, String password) {
        userService.add(userDTO, password);
        return "saved_user";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users",userService.findAll());
        return "users";
    }

    @GetMapping("/login-profile")
    public String me(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO()); // Add all users to the model
        return "profile"; // Return the "profile" view to render the of user page
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.remove(id);
        return "deleted_user";
    }

    @PostMapping("/login-profile/delete")
    public String deleteMyUser() {
        userService.remove(this.userService.getLoggedUserDTO().id());
        return "deleted_user";
    }

    @GetMapping("/login-profile/edit")
    public String editMyUser(Model model) {
        model.addAttribute("user", this.userService.getLoggedUserDTO());
        return "edit_user";
    }

    @PostMapping("/login-profile/edit")
    public String editUser(UserDTO userDTO, String password){
        userService.update(userDTO, password);
        return "redirect:/login-profile";
    }
}
