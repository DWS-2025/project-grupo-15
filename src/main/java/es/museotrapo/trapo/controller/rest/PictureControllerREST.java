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

    // Injecting the PictureService to manage picture-related functionality
    @Autowired
    private PictureService pictureService;

    /**
     * Endpoint to retrieve all pictures.
     *
     * @return a collection of PictureDTOs.
     */
    @GetMapping("/")
    public Collection<PictureDTO> getPictures() {
        return pictureService.getPictures(); // Retrieve all pictures as DTOs
    }

    /**
     * Endpoint to retrieve a picture by its ID.
     *
     * @param id the ID of the picture to retrieve.
     * @return the PictureDTO of the picture with the given ID.
     */
    @GetMapping("/{id}")
    public PictureDTO getPicture(@PathVariable long id) {
        return pictureService.getPicture(id); // Retrieve the picture by ID as DTO
    }

    /**
     * Endpoint to create a new picture.
     * It uses a PictureDTO to create a new picture and associate it with an artist.
     *
     * @param PictureDTO the picture data to be created.
     * @return ResponseEntity with the created PictureDTO and location of the newly created picture.
     * @throws IOException if there is an issue handling the image.
     */
    @PostMapping("/")
    public ResponseEntity<PictureDTO> createPicture(@RequestBody PictureDTO PictureDTO) throws IOException {
        // Create a new picture using the provided DTO
        PictureDTO = pictureService.createPictureREST(PictureDTO);

        // Construct the URI for the newly created picture
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(PictureDTO.id()).toUri();

        // Return the created picture along with the location header
        return ResponseEntity.created(location).body(PictureDTO);
    }

    /**
     * Endpoint to upload an image for a specific picture.
     *
     * @param id the ID of the picture.
     * @param imageFile the image file to upload.
     * @return ResponseEntity with the location header.
     * @throws IOException if there is an issue handling the image.
     */
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createPostImage(@PathVariable long id, @RequestParam MultipartFile imageFile) 
            throws IOException {

        URI location = fromCurrentRequest().build().toUri();

        // Create the picture image using the provided file input stream
        pictureService.createPictureImageREST(id, location, imageFile.getInputStream(), imageFile.getSize());

        // Return a successful response indicating the picture's image was uploaded
        return ResponseEntity.created(location).build();
    }

    /**
     * Endpoint to delete a specific picture by its ID.
     *
     * @param id the ID of the picture to delete.
     * @return the PictureDTO of the deleted picture.
     */
    @DeleteMapping("/{id}")
    public PictureDTO deletePicture(@PathVariable long id) {
        // Delete the picture and return the deleted PictureDTO
        return pictureService.deletePicture(getPicture(id));
    }

    /**
     * Endpoint to retrieve the image of a specific picture by its ID.
     *
     * @param id the ID of the picture.
     * @return the image file of the picture.
     * @throws SQLException if there is an error retrieving the image.
     * @throws IOException if there is an issue with the image.
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {

        // Retrieve the image associated with the picture
        Resource postImage = (Resource) pictureService.getPictureImage(id);

        // Return the image as the response body with the appropriate content type
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);
    }

}

