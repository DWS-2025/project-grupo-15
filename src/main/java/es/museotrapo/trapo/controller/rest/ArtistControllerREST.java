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

    // Injecting the ArtistService to manage artist-related functionality
    @Autowired
    private ArtistService artistService;

    Pageable defaultPage = PageRequest.of(0, 2); // Default pagination settings (page 0, size 10)
    /**
     * Endpoint to retrieve all artists with pagination support.
     *
     * @param pageable the pagination information (page number, size, sorting).
     * @return a page of ArtistDTOs.
     */
    @GetMapping("")
    public Page<ArtistDTO> getArtists(Pageable pageable) {
        return artistService.getArtists(pageable); // Retrieve artists with pagination
    }

    /**
     * Endpoint to retrieve a specific artist by its ID.
     *
     * @param id the ID of the artist.
     * @return the ArtistDTO of the artist with the given ID.
     */
    @GetMapping("/{id}")
    public ArtistDTO getArtist(@PathVariable long id) {
        return artistService.getArtist(id); // Retrieve the artist by ID as DTO
    }

    /**
     * Endpoint to create a new artist.
     *
     * @param artistDTO the artist data to be created.
     * @return ResponseEntity with the created ArtistDTO and location of the newly created artist.
     */
    @PostMapping("")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistDTO artistDTO) {
        // Create a new artist using the provided DTO
        artistDTO = artistService.createArtist(artistDTO);

        // Construct the URI for the newly created artist
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(artistDTO.id()).toUri();

        // Return the created artist along with the location header
        return ResponseEntity.created(location).body(artistDTO);
    }

    /**
     * Endpoint to delete a specific artist by its ID.
     *
     * @param id the ID of the artist to delete.
     * @return the ArtistDTO of the deleted artist.
     */
    @DeleteMapping("/{id}")
    public ArtistDTO deleteArtist(@PathVariable long id) {
        return artistService.deleteArtist(id); // Delete the artist and return the deleted ArtistDTO
    }

    /**
     * Endpoint to update an artist with new data.
     *
     * @param id               the ID of the artist to update.
     * @param updatedArtistDTO the new artist data.
     * @return the updated ArtistDTO.
     */
    @PutMapping("/{id}")
    public ArtistDTO replaceArtist(@PathVariable long id, @RequestBody ArtistDTO updatedArtistDTO) {
        return artistService.replaceArtist(id, updatedArtistDTO); // Update the artist and return the updated DTO
    }

    @GetMapping("/{id}/biography")
    public ResponseEntity<Resource> getBiography(@PathVariable long id) {
        try{
            return artistService.getBiographyResponse(id);
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File was not found.", e);
        } // Retrieve the biography of the artist by ID
    }

    @PostMapping("/{id}/biography")
    public MultipartFile uploadBiography(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        artistService.saveBiography(id, file);
        return file;
    }
}

