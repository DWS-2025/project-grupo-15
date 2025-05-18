package es.museotrapo.trapo.controller.web;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.museotrapo.trapo.dto.ArtistDTO;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.service.ArtistService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller responsible for handling actions related to `Artist` entities.
 * Manages artist querying, creation, editing, deletion, and file uploads such as biographies.
 */
@Controller
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    /**
     * Displays a list of all artists with optional filtering by attributes.
     * Supports pagination with a specified page size.
     *
     * @param model     The model to add attributes for rendering in the view.
     * @param name      Filter optional: Search artists by name.
     * @param nickname  Filter optional: Search artists by nickname.
     * @param birthDate Filter optional: Search artists by birth date.
     * @param artistPage The `Pageable` object to handle pagination.
     * @return The view name "artists" which displays the artist list.
     */
    @GetMapping("")
    public String getArtists(Model model,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String nickname,
                             @RequestParam(required = false) String birthDate,
                             Pageable artistPage) {
        int pageSize = 3; // Specify the number of artists per page
        artistPage = PageRequest.of(artistPage.getPageNumber(), pageSize);

        // If any filter parameter is provided, query filtered results
        if (name != null || nickname != null || birthDate != null) {
            Artist artist = new Artist();
            artist.setName(name);
            artist.setNickname(nickname);
            artist.setBirthDate(birthDate);

            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnorePaths("id", "biography");
            Example<Artist> example = Example.of(artist, matcher);

            Page<ArtistDTO> artists = artistService.getArtists(example, artistPage);
            model.addAttribute("artists", artists);
        } else {
            // Else retrieve all artists
            Page<ArtistDTO> artists = artistService.getArtists(artistPage);
            model.addAttribute("artists", artists);
        }

        // Add pagination attributes for navigation
        boolean hasPrev = artistPage.getPageNumber() >= 1;
        boolean hasNext = (artistPage.getPageNumber() * artistPage.getPageSize()) < artistService.count();

        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", artistPage.getPageNumber() - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", artistPage.getPageNumber() + 1);

        return "artists";
    }

    /**
     * Fetches additional artists for infinite scrolling/pagination.
     * Returns the result as a JSON response body.
     *
     * @param page The current page number (default is 0).
     * @return A list of `ArtistDTO` objects for the requested page.
     */
    @GetMapping("/more")
    @ResponseBody
    public List<ArtistDTO> getMoreArtists(@RequestParam(defaultValue = "0") int page) {
        int pageSize = 3; // Number of artists per page
        Page<ArtistDTO> artistPage = artistService.getArtists(PageRequest.of(page, pageSize));
        return artistPage.getContent(); // Return a paginated result set
    }

    /**
     * Shows the form to create a new artist.
     *
     * @param model The model to include attributes for display in the form.
     * @return The view name "form_artist" which displays the form.
     */
    @GetMapping("/new")
    public String newArtist(Model model) {
        model.addAttribute("artist", new Artist()); // Initialize an empty artist object
        model.addAttribute("isEdit", false); // Indicate that this is a creation form
        return "form_artist";
    }

    /**
     * Processes form submission to create a new artist.
     *
     * @param model     The model to add attributes to.
     * @param artistDTO The data submitted from the form for the new artist.
     * @return The view name "saved_artist" upon successful creation.
     */
    @PostMapping("/new")
    public String newArtist(Model model, ArtistDTO artistDTO) {
        artistService.createArtist(artistDTO); // Persist new artist information
        return "saved_artist";
    }

    /**
     * Retrieves and displays details of a specific artist by ID.
     *
     * @param model The model to add artist details for rendering in the view.
     * @param id    The ID of the artist to fetch details for.
     * @return The view name "show_artist" if found, otherwise "artist_not_found".
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
     * Deletes an artist by ID.
     *
     * @param id The ID of the artist to be deleted.
     * @return The view name "deleted_artist" if successful; otherwise, "artist_not_found".
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
     * Displays the form for editing an artist by ID.
     *
     * @param model The model to add the current artist's details.
     * @param id    The ID of the artist to edit.
     * @return The view name "form_artist" or "artist_not_found" if not available.
     */
    @GetMapping("/{id}/edit")
    public String editArtist(Model model, @PathVariable long id) {
        try {
            ArtistDTO artist = artistService.getArtist(id);
            model.addAttribute("artist", artist);
            model.addAttribute("isEdit", true); // Indicate that this is an edit form
            return "form_artist";
        } catch (NoSuchElementException e) {
            return "artist_not_found";
        }
    }

    /**
     * Handles the update of an artist's information via form submission.
     *
     * @param model             The model to add updated attributes.
     * @param updatedArtistDTO  The new data to replace the artist's information.
     * @return A redirect to the updated artist's profile page.
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

    /**
     * Uploads a biography file for a specified artist.
     *
     * @param id    The ID of the artist.
     * @param file  The uploaded biography file.
     * @return A view name indicating successful biography save.
     * @throws IOException If there is an error processing the file.
     */
    @PostMapping("/{id}/biography")
    public String uploadBiography(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        artistService.saveBiography(id, file); // Save the biography file
        return "saved_biography";
    }

    /**
     * Retrieves the biography of a specified artist from the system.
     *
     * @param id The ID of the artist.
     * @return A ResponseEntity containing the biography file as a `Resource`.
     * @throws ResponseStatusException If the file is not found or there is an I/O issue.
     */
    @GetMapping("/{id}/biography")
    public ResponseEntity<Resource> getBiography(@PathVariable Long id) {
        try {
            return artistService.getBiographyResponse(id);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File was not found.", e);
        }
    }
}