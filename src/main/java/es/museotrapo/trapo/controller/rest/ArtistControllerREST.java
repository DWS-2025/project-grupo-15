package es.museotrapo.trapo.controller.rest;

import java.io.IOException;
import java.net.URI;
//import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.museotrapo.trapo.service.ArtistService;
import es.museotrapo.trapo.dto.ArtistDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/artists")
public class ArtistControllerREST {

    // Injecting the ArtistService to handle business logic related to artists
    @Autowired
    private ArtistService artistService;

    // Default pagination settings: page 0 and size 2 (modifiable globally if needed)
    Pageable defaultPage = PageRequest.of(0, 2);

    /**
     * Retrieves all artists with pagination support.
     *
     * @param pageable the pagination information (page number, size, sorting).
     * @return a page of ArtistDTOs.
     */
    @GetMapping("")
    public Page<ArtistDTO> getArtists(Pageable pageable) {
        return artistService.getArtists(pageable); // Fetch artists using pagination information
    }

    /**
     * Retrieves a specific artist by its ID.
     *
     * @param id the ID of the artist.
     * @return the ArtistDTO of the artist with the given ID.
     */
    @GetMapping("/{id}")
    public ArtistDTO getArtist(@PathVariable long id) {
        return artistService.getArtist(id); // Fetch artist data by ID
    }

    /**
     * Creates a new artist with the provided data.
     *
     * @param artistDTO the artist data to be created.
     * @return ResponseEntity containing the new ArtistDTO and the location of the created artist.
     */
    @PostMapping("")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistDTO artistDTO) {
        // Create the artist using the provided DTO
        artistDTO = artistService.createArtist(artistDTO);

        // Build URI pointing to the newly created artist
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(artistDTO.id()).toUri();

        // Return the newly created artist along with the URI in the location header
        return ResponseEntity.created(location).body(artistDTO);
    }

    /**
     * Deletes a specific artist by its ID.
     *
     * @param id the ID of the artist to delete.
     * @return the ArtistDTO of the deleted artist.
     */
    @DeleteMapping("/{id}")
    public ArtistDTO deleteArtist(@PathVariable long id) {
        return artistService.deleteArtist(id); // Delete the artist and return their data as DTO
    }

    /**
     * Updates an existing artist with new data.
     *
     * @param id               the ID of the artist to update.
     * @param updatedArtistDTO the new artist data.
     * @return the updated ArtistDTO.
     */
    @PutMapping("/{id}")
    public ArtistDTO replaceArtist(@PathVariable long id, @RequestBody ArtistDTO updatedArtistDTO) {
        return artistService.replaceArtist(id, updatedArtistDTO); // Modify the artist's data and return the updated DTO
    }

    /**
     * Retrieves the biography of a specific artist.
     *
     * @param id the ID of the artist whose biography is requested.
     * @return a ResponseEntity containing the biography file as a resource.
     */
    @GetMapping("/{id}/biography")
    public ResponseEntity<Resource> getBiography(@PathVariable long id) {
        try {
            // Retrieve the artist's biography file
            return artistService.getBiographyResponse(id);
        } catch (IOException e) {
            // Throw an exception if the file isn't found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File was not found.", e);
        }
    }

    /**
     * Uploads a biography file for a specific artist.
     *
     * @param id   the ID of the artist whose biography is being uploaded.
     * @param file the biography file (as a MultipartFile).
     * @return a ResponseEntity with the location of the uploaded file.
     * @throws IOException if an error occurs during file upload.
     */
    @PostMapping("/{id}/biography")
    public ResponseEntity<Object> uploadBiography(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        // Save the uploaded biography file for the artist
        artistService.saveBiography(id, file);

        // Return a ResponseEntity with the location of the uploaded file
        URI location = fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }
}