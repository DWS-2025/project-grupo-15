package es.museotrapo.trapo.controller;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.museotrapo.trapo.dto.ArtistDTO;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.service.ArtistService;

@Controller
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    /**
     * Handles the request to display all artists.
     *
     * @param model The model to add attributes to
     * @return The view name "artists"
     */
    @GetMapping("/artists")
    public String getArtists(Model model) {
        // Fetch all artists from the artistService and add them to the model
        model.addAttribute("artists", artistService.getArtists());
        return "artists"; // Return the view name
    }

    /**
     * Displays the form to create a new artist.
     *
     * @param model The model to add attributes to
     * @return The view name "new_artist"
     */
    @GetMapping("/artist/new")
    public String newArtist(Model model) {
        model.addAttribute("artist", new Artist()); // Add an empty Artist object to the model
        model.addAttribute("isEdit", false); // Set isEdit to false to indicate that the form is for creation
        return "form_artist"; // Return the form view
    }

    /**
     * Handles the submission of the form to create a new artist.
     *
     * @param model  The model to add attributes to
     * @param artist The artist object to be saved
     * @return The view name "saved_artist"
     */
    @PostMapping("/artist/new")
    public String newArtist(Model model, ArtistDTO artistDTO) {
        artistService.createArtist(artistDTO); // Save the new artist using the artistService
        return "saved_artist"; // Return the view after saving the artist
    }

    /**
     * Displays a specific artist based on the provided ID.
     *
     * @param model The model to add attributes to
     * @param id    The ID of the artist to fetch
     * @return The view name depending on whether the artist is found
     */
    @GetMapping("/artist/{id}")
    public String getArtist(Model model, @PathVariable long id) {
        
        try {
			ArtistDTO artist = artistService.getArtist(id);
			model.addAttribute("artist", artist);
			return "show_artist";	

		} catch (NoSuchElementException e){
			return "artist_not_found";
		}
    }

    /**
     * Handles the deletion of an artist by ID.
     *
     * @param id The ID of the artist to delete
     * @return The view name depending on whether the artist is found
     */
    @PostMapping("/artist/{id}/delete")
    public String deleteArtist(@PathVariable long id) {

        try {
		
			artistService.deleteArtist(id);
			return "deleted_artist";

		} catch (NoSuchElementException e){
			return "artist_not_found";
		}
    }

    /**
     * Displays the form to edit an artist based on the provided ID.
     *
     * @param model The model to add attributes to
     * @param id    The ID of the artist to edit
     * @return The view name depending on whether the artist is found
     */
    @GetMapping("/artist/{id}/edit")
    public String editArtist(Model model, @PathVariable long id) {

        try {
			ArtistDTO artist = artistService.getArtist(id);
			model.addAttribute("artist", artist);
			return "form_artist";	

		} catch (NoSuchElementException e){
			return "artist_not_found";
		}
    }

    /**
     * Handles the submission of the form to update an artist.
     *
     * @param model         The model to add attributes to
     * @param id            The ID of the artist to update
     * @param updatedArtist The updated artist data
     * @return A redirect to the updated artist's page
     */
    @PostMapping("/artist/{id}/edit")
    public String updateArtist(Model model, ArtistDTO updatedArtistDTO) {

        try {
		
			artistService.replaceArtist(updatedArtistDTO.id(), updatedArtistDTO);
			model.addAttribute("artist", updatedArtistDTO);
			return "redirect:/artist/" + updatedArtistDTO.id();

		} catch (NoSuchElementException e){
			return "artist_not_found";
		}
    }
}