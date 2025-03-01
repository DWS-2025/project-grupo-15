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




@Controller
public class ArtistController {

    @Autowired
    private ArtistService artistService;


    @GetMapping("/artists")
    public String getArtists(Model model){
        model.addAttribute("artists", artistService.findAll());
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
		Optional<Artist> artist = artistService.findById(id);
        if(artist.isPresent()){
            model.addAttribute("artist", artist.get());
		    return "show_artist";
        } else {
            return "artist_not_found";
        }
	}

    @PostMapping("/artist/{id}/delete")
	public String deleteArtist(@PathVariable long id) {
	    Optional<Artist> artist = artistService.findById(id);
        if(artist.isPresent()){
            artistService.delete(artist.get());
		    return "deleted_artist";
        } else {
            return "artist_not_found";
        }
	}

    @GetMapping("/artist/{id}/edit")
	public String editArtist(Model model, @PathVariable long id) {
		Optional<Artist> artist = artistService.findById(id);
		if (artist.isPresent()) {
			model.addAttribute("artist", artist.get());
			return "edit_artist";
		} else {
			return "artist_not_found";
		}
	}

    @PostMapping("/artist/{id}/edit")
	public String updateArtist(Model model, @PathVariable long id, Artist updatedArtist) {
		Optional<Artist> artist = artistService.findById(id);
		if (artist.isPresent()) {
			Artist oldArtist = artist.get();
			artistService.update(oldArtist, updatedArtist);
			return "redirect:/artist/" + id;
		} else {
			return "artist_not_found";
		}
	}

}
