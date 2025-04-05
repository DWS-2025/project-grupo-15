package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.service.PictureService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
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
    public ResponseEntity<PictureDTO> createPicture(@RequestBody PictureDTO PictureDTO) throws IOException{

         PictureDTO = pictureService.createPictureREST(PictureDTO);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(PictureDTO.id()).toUri();

        return ResponseEntity.created(location).body(PictureDTO);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createPostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        URI location = fromCurrentRequest().build().toUri();

        pictureService.createPictureImageREST(id, location, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/{id}")
	public PictureDTO deletePicture(@PathVariable long id) {

        return pictureService.deletePicture(getPicture(id));
	}

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {

        Resource postImage = (Resource) pictureService.getPictureImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);

    }

}
