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

        if(name != null || nickname != null || birthDate != null) {
			Artist artist = new Artist();
			artist.setName(name);
            artist.setNickname(nickname);
            artist.setBirthDate(birthDate);
			ExampleMatcher matcher = ExampleMatcher.matching()
											.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
											.withIgnorePaths("id", "biography");
			Example<Artist> example = Example.of(artist, matcher);
            System.out.println("Estamos devolviendo todos los artistas que coincidan con el ejemplo "+example);
			Page<ArtistDTO> artists = artistService.getArtists(example, artistPage);
            model.addAttribute("artists", artists);
		}else{
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

    /**
     * Handles the request to fetch more artists for pagination.
     *
     * @param page The page number to fetch
     * @return A list of ArtistDTO objects
     */
    @GetMapping("/more")
    @ResponseBody
    public List<ArtistDTO> getMoreArtists(@RequestParam(defaultValue = "0") int page) {
    int pageSize = 3; // Number of artists per page
    Page<ArtistDTO> artistPage = artistService.getArtists(PageRequest.of(page, pageSize));
    return artistPage.getContent();
}


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

    /**
     * Handles the upload of an artist's biography.
     *
     * @param id   The ID of the artist
     * @param file The uploaded file
     * @return The view name "saved_biography"
     * @throws IOException If an error occurs during file upload
     */
    @PostMapping("/{id}/biography")
    public String uploadBiography(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        artistService.saveBiography(id, file);
        return "saved_biography";
    }

    /**
     * Handles the request to fetch an artist's biography.
     *
     * @param id The ID of the artist
     * @return A ResponseEntity containing the biography file
     */
    @GetMapping("/{id}/biography")
    public ResponseEntity<Resource> getBiography(@PathVariable Long id){
        try{
            return artistService.getBiographyResponse(id);
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File was not found.", e);
        }
    }
}
