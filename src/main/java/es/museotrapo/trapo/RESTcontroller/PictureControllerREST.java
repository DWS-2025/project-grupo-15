package es.museotrapo.trapo.RESTcontroller;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;


@RestController
@RequestMapping("/api/pictures")
public class PictureControllerREST {

    @Autowired
    private PictureService pictureService; // Service to handle picture-related functionality


    @GetMapping("/")
    public Collection <PictureDTO> getPictures() {

        return pictureService.getPictures();
    }

    @GetMapping("/{id}")
    public PictureDTO getPicture(@PathVariable long id) {

        return pictureService.getPicture(id);
    }

    @PostMapping("/")
    public ResponseEntity<PictureDTO> createArtist(@RequestBody PictureDTO PictureDTO, Long artistId, MultipartFile imageFile) throws IOException{
        
        PictureDTO = pictureService.createPicture(PictureDTO, artistId, imageFile);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(PictureDTO.id()).toUri();
        
        return ResponseEntity.created(location).body(PictureDTO);
    }

    @DeleteMapping("/{id}")
	public PictureDTO deletePicture(@PathVariable long id) {

        return pictureService.deletePicture(getPicture(id));
	}

}
