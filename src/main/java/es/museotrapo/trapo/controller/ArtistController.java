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

    /**
     * Handles the request to display all artists.
     *
     * @param model The model to add attributes to
     * @return The view name "artists"
     */
    @GetMapping("/artists")
    public String getArtists(Model model) {
        // Fetch all artists from the artistService and add them to the model
        model.addAttribute("artists", artistService.findAll());
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
    public String newArtist(Model model, Artist artist) {
        artistService.save(artist); // Save the new artist using the artistService
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
        Optional<Artist> artist = artistService.findById(id);
        if (artist.isPresent()) {
            model.addAttribute("artist", artist.get());
            return "show_artist"; // If the artist is found, display the artist's details
        } else {
            return "artist_not_found"; // If not found, show a not found page
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
        Optional<Artist> artist = artistService.findById(id);
        if (artist.isPresent()) {
            artistService.delete(artist.get()); // Delete the artist using the artistService
            return "deleted_artist"; // Return the view after deletion
        } else {
            return "artist_not_found"; // If artist is not found, show not found page
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
        Optional<Artist> artist = artistService.findById(id);
        if (artist.isPresent()) {
            model.addAttribute("artist", artist.get()); // Add the found artist to the model
            model.addAttribute("isEdit", true); // Set isEdit to true to indicate editing mode
            return "form_artist"; // Return the form view for editing
        } else {
            return "artist_not_found"; // Return the not found page if the artist does not exist
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
    public String updateArtist(Model model, @PathVariable long id, Artist updatedArtist) {
        Optional<Artist> artist = artistService.findById(id);
        if (artist.isPresent()) {
            Artist oldArtist = artist.get();
            artistService.update(oldArtist, updatedArtist); // Update the artist with new data
            return "redirect:/artist/" + id; // Redirect to the artist's details page after update
        } else {
            return "artist_not_found"; // If artist is not found, show not found page
        }
    }
}