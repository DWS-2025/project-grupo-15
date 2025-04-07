package es.museotrapo.trapo.controller.web;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.museotrapo.trapo.dto.ArtistDTO;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.service.ArtistService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    /**
     * Handles the request to display all artists.
     *
     * @param model The model to add attributes to
     * @return The view name "artists"
     */
    @GetMapping("")
    public String getArtists(Model model,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String nickname,
                             @RequestParam(required = false) String birthDate,
                             Pageable artistPage) {
        int pageSize = 3;

        artistPage = PageRequest.of(artistPage.getPageNumber(), pageSize);

        if (name != null || nickname != null || birthDate != null) {
            Page<ArtistDTO> artists = artistService.searchArtists(name, nickname, birthDate, artistPage);
            model.addAttribute("artists", artists);
        } else {
            Page<ArtistDTO> artists = artistService.getArtists(artistPage);
            model.addAttribute("artists", artists);
        }

        boolean hasPrev = artistPage.getPageNumber() >= 1;
        boolean hasNext = (artistPage.getPageNumber() * artistPage.getPageSize()) < artistService.count();

        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", artistPage.getPageNumber() - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", artistPage.getPageNumber() + 1);

        return "artists"; // Return the view name
    }

    /* 
    @GetMapping("/more")
    @ResponseBody
    public List<ArtistDTO> getMoreArtists(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;
        Page<ArtistDTO> productPage = artistService.getArtists(PageRequest.of(page, pageSize));
        return productPage.getContent();
    }
    */

    /**
     * Displays the form to create a new artist.
     *
     * @param model The model to add attributes to
     * @return The view name "new_artist"
     */
    @GetMapping("/new")
    public String newArtist(Model model) {
        model.addAttribute("artist", new Artist()); // Add an empty Artist object to the model
        model.addAttribute("isEdit", false); // Set isEdit to false to indicate that the form is for creation
        return "form_artist"; // Return the form view
    }

    /**
     * Handles the submission of the form to create a new artist.
     *
     * @return The view name "saved_artist"
     */
    @PostMapping("/new")
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
    @GetMapping("/{id}")
    public String getArtist(Model model, @PathVariable long id) {

        try {
            ArtistDTO artist = artistService.getArtist(id);
            model.addAttribute("artist", artist);
            return "show_artist";

        } catch (NoSuchElementException e) {
            return "artist_not_found";
        }
    }

    /**
     * Handles the deletion of an artist by ID.
     *
     * @param id The ID of the artist to delete
     * @return The view name depending on whether the artist is found
     */
    @PostMapping("/{id}/delete")
    public String deleteArtist(@PathVariable long id) {

        try {
            artistService.deleteArtist(id);
            return "deleted_artist";

        } catch (NoSuchElementException e) {
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
    @GetMapping("/{id}/edit")
    public String editArtist(Model model, @PathVariable long id) {

        try {
            ArtistDTO artist = artistService.getArtist(id);
            model.addAttribute("artist", artist);
            model.addAttribute("isEdit", true); // Set isEdit to true to indicate that the form is for edit
            return "form_artist";

        } catch (NoSuchElementException e) {
            return "artist_not_found";
        }
    }

    /**
     * Handles the submission of the form to update an artist.
     *
     * @param model The model to add attributes to
     * @return A redirect to the updated artist's page
     */
    @PostMapping("/{id}/edit")
    public String updateArtist(Model model, ArtistDTO updatedArtistDTO) {

        try {

            artistService.replaceArtist(updatedArtistDTO.id(), updatedArtistDTO);
            model.addAttribute("artist", updatedArtistDTO);
            return "redirect:/artists/" + updatedArtistDTO.id();

        } catch (NoSuchElementException e) {
            return "artist_not_found";
        }
    }
}
