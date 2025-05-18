package es.museotrapo.trapo.controller.rest;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.service.PictureService;
import es.museotrapo.trapo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

/**
 * REST controller for managing pictures and their related functionalities.
 * Provides endpoints for CRUD operations, uploading images, comments, and likes.
 */
@RestController
@RequestMapping("/api/pictures")
public class PictureControllerREST {

    // Service to handle picture-related operations
    @Autowired
    private PictureService pictureService;

    // Service to manage user-related functionality
    @Autowired
    private UserService userService;

    /**
     * Retrieves all pictures.
     *
     * @return a collection of PictureDTO objects representing all pictures.
     */
    @GetMapping("")
    public Collection<PictureDTO> getPictures() {
        return pictureService.getPictures();
    }

    /**
     * Retrieves a specific picture by its ID.
     *
     * @param id the ID of the picture to retrieve.
     * @return the PictureDTO object representing the requested picture.
     */
    @GetMapping("/{id}")
    public PictureDTO getPicture(@PathVariable long id) {
        return pictureService.getPicture(id);
    }

    /**
     * Retrieves the image file associated with a specific picture.
     *
     * @param id the ID of the picture.
     * @return the image file as a resource wrapped in the ResponseEntity.
     * @throws SQLException if there is an error retrieving the image from the database.
     * @throws IOException  if there is an issue with handling the output.
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {
        Resource postImage = pictureService.getPictureImage(id);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);
    }

    /**
     * Retrieves all comments associated with a specific picture.
     *
     * @param id the ID of the picture.
     * @return a collection of CommentDTO objects representing the comments for the picture.
     * @throws IOException if there is an issue retrieving the comments.
     */
    @GetMapping("/{id}/comments")
    public Collection<CommentDTO> getComments(@PathVariable long id) throws IOException {
        return pictureService.getComments(id);
    }

    /**
     * Creates a new picture based on the provided PictureDTO.
     *
     * @param PictureDTO the object containing picture data.
     * @return a ResponseEntity containing the created PictureDTO and its URI.
     * @throws IOException if there is an error handling the creation.
     */
    @PostMapping("")
    public ResponseEntity<PictureDTO> createPicture(@RequestBody PictureDTO PictureDTO) throws IOException {
        PictureDTO = pictureService.createPictureREST(PictureDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(PictureDTO.id()).toUri();
        return ResponseEntity.created(location).body(PictureDTO);
    }

    /**
     * Uploads an image for a specific picture.
     *
     * @param id the ID of the picture.
     * @param imageFile the image file to be uploaded.
     * @return a ResponseEntity with the URI of the image.
     * @throws IOException if there is an error handling the file upload.
     */
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createPostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {
        URI location = fromCurrentRequest().build().toUri();
        pictureService.createPictureImageREST(id, location, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    /**
     * Adds a comment to a specific picture.
     *
     * @param id the ID of the picture.
     * @param commentDTO the comment data to be added.
     * @return a ResponseEntity containing the created CommentDTO and its URI.
     * @throws IOException if there is an error handling the comment addition.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable long id, @RequestBody CommentDTO commentDTO) throws IOException {
        commentDTO = pictureService.addComment(commentDTO, id);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(commentDTO.id()).toUri();
        return ResponseEntity.created(location).body(commentDTO);
    }

    /**
     * Adds or removes a like for a specific picture by the current user.
     *
     * @param id the ID of the picture to like or unlike.
     * @return a ResponseEntity with the updated PictureDTO.
     * @throws IOException if there is an issue handling the like operation.
     */
    @PostMapping("/{id}/likes")
    public ResponseEntity<PictureDTO> giveLike(@PathVariable long id) throws IOException {
        PictureDTO pictureDTO = pictureService.getPicture(id);
        userService.likeOrRemovePicture(pictureDTO);
        pictureDTO = pictureService.getPicture(id);
        return ResponseEntity.ok(pictureDTO);
    }

    /**
     * Deletes a specific picture by its ID.
     *
     * @param id the ID of the picture to delete.
     * @return the PictureDTO of the deleted picture.
     */
    @DeleteMapping("/{id}")
    public PictureDTO deletePicture(@PathVariable long id) {
        return pictureService.deletePicture(getPicture(id));
    }

    /**
     * Deletes a specific comment on a picture.
     *
     * @param id the ID of the picture.
     * @param commentId the ID of the comment to delete.
     * @param authentication the current user's authentication details.
     * @return the CommentDTO of the deleted comment.
     * @throws IOException if there is an issue handling the deletion.
     */
    @DeleteMapping("/{id}/comments/{commentId}")
    public CommentDTO deleteComment(@PathVariable long id, @PathVariable long commentId, Authentication authentication) throws IOException {
        return pictureService.removeComment(commentId, id, authentication);
    }
}