package es.museotrapo.trapo.controller;

import org.springframework.ui.Model;
import es.museotrapo.trapo.service.ArtistService;
import es.museotrapo.trapo.service.CommentService;
import es.museotrapo.trapo.service.PictureService;
import es.museotrapo.trapo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String getPosts(Model model){
        model.addAttribute("Pictures", pictureService.getPictures());
        return "index";
    }

    @GetMapping("/pictures/new")
    public String newPicture(Model model){

    }

}
