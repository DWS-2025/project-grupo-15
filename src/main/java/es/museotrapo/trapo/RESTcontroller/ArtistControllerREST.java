package es.museotrapo.trapo.RESTcontroller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.museotrapo.trapo.service.ArtistService;
import es.museotrapo.trapo.dto.ArtistDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/artists")
public class ArtistControllerREST {

    @Autowired 
    private ArtistService artistService;

    @GetMapping("/")
    public Collection <ArtistDTO> getArtists() {

        return artistService.getArtists();
    }
    
    @GetMapping("/{id}")
    public ArtistDTO getArtist(@PathVariable long id) {

        return artistService.getArtist(id);
    }

    @PostMapping("/")
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody ArtistDTO artistDTO) {
        
        artistDTO = artistService.createArtist(artistDTO);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(artistDTO.id()).toUri();
        
        return ResponseEntity.created(location).body(artistDTO);
    }
    
    @DeleteMapping("/{id}")
	public ArtistDTO deleteArtist(@PathVariable long id) {

        return artistService.deleteArtist(id);
	}

    @PutMapping("/{id}")
	public ArtistDTO replaceArtist(@PathVariable long id, @RequestBody ArtistDTO updatedArtistDTO) {

        return artistService.replaceArtist(id, updatedArtistDTO);
	}
}
