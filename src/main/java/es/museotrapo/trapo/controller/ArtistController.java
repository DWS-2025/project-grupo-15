package es.museotrapo.trapo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.service.ArtistService;
import es.museotrapo.trapo.service.CommentService;
import es.museotrapo.trapo.service.PictureService;
import es.museotrapo.trapo.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ArtistController {

      @Autowired
    private PictureService pictureService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


     @GetMapping("/artists")
    public String getArtists(Model model){
        model.addAttribute("artists", artistService.getArtists());
        return "artists";
    }

    @GetMapping("/artist/new")
    public String newArtist(Model model){
        return "new_artist";
    }
    
    @PostMapping("/artist/new")
    public String newArtist(Model model, Artist artist){
        artistService.save(artist);
        return "saved_artist";
    }

    @GetMapping("/artist/{id}")
	public String getArtist(Model model, @PathVariable long id) {
		Artist artist = artistService.getArtistById(id);
		model.addAttribute("artist", artist);
		return "show_artist";
	}


    @PostMapping("/artist/{id}/delete")
	public String deleteArtist(@PathVariable long id) {
	    Artist artist = artistService.getArtistById(id);
		artistService.deleteArtist(artist);
		return "redirect:/artists";
	}



}
