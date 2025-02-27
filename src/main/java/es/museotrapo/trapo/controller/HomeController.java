package es.museotrapo.trapo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String principal() {
        return "index"; //Muestra la página principal (index.html)
    }
}
