package es.museotrapo.trapo.controller;

import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.service.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/picture") // Maps the controller to "/picture" endpoint
public class PictureController {

    private static final Path PICTURE_PATH = Paths.get(System.getProperty("user.dir"), "pictures");

    @Autowired
    private PictureService pictureService; // Service to handle picture-related functionality

    @Autowired
    private ArtistService artistService; // Service to handle artist-related functionality

    @Autowired
    private UserService userService; // Service to handle user-related functionality

    @Autowired
    private CommentService commentService; // Service to handle comment-related functionality

    @Autowired
    private ImageService imageService; // Service to handle image upload functionality

    /**
     * Handles the GET request to display all pictures
     * 
     * @param model Model object to add attributes for the view
     * @return "pictures" view to render all pictures
     */
    @GetMapping("")
    public String getPictures(Model model) {
        model.addAttribute("pictures", pictureService.findAll()); // Add all pictures to the model
        return "pictures"; // Return the "pictures" view to render the pictures
    }

    /**
     * Handles the GET request to display the form to add a new picture
     * 
     * @param model Model object to add available artists to the view
     * @return "new_picture" view to display the form
     */
    @GetMapping("/new")
    public String newPicture(Model model) {
        model.addAttribute("availableArtists", artistService.findAll()); // Add available artists to the model
        return "new_picture"; // Return the "new_picture" view to display the form
    }

    /**
     * Handles the POST request to submit a new picture
     * 
     * @param model     Model object to add attributes for the view
     * @param picture   The picture object to be saved
     * @param imageFile The image file to be uploaded
     * @param artistID  The ID of the artist associated with the picture
     * @return "saved_picture" view to show the saved picture
     * @throws IOException If an error occurs while handling the image file
     */
    @PostMapping("/new")
    public String newPicture(Model model,
            Picture picture, // The picture object to be saved
            @RequestParam MultipartFile imageFile, // Image file associated with the picture
            @RequestParam Long artistID) throws IOException { // ID of the artist

        pictureService.save(picture, artistID, imageFile); // Save the picture in the database
        model.addAttribute("picture", picture); // Add the saved picture to the model
        return "saved_picture"; // Redirect to "saved_picture" view after saving
    }

    /**
     * Handles the GET request to display a single picture by its ID
     * 
     * @param model Model object to add attributes for the view
     * @param id    The ID of the picture to be displayed
     * @return "show_picture" view to display the picture details, or
     *         "picture_not_found" if not found
     */
    @GetMapping("/{id}")
    public String getPicture(Model model, @PathVariable long id) {
        Optional<Picture> picture = pictureService.findById(id); // Fetch the picture by ID
        if (picture.isPresent()) {
            model.addAttribute("picture", picture.get()); // Add the picture to the model
            String likedPicture = userService.isPictureLiked(picture.get()) ? "Dislike" : "Like"; // Check if the picture
                                                                                                 // is liked by the user
            model.addAttribute("likedPicture", likedPicture); // Add like status to the model
            model.addAttribute("imagePath", "/picture/" + picture.get().getImageFilename()); // Add image path to the
                                                                                             // model
            return "show_picture"; // Return the "show_picture" view to display the picture details
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if picture does not exist
        }
    }

    /**
     * Handles the GET request to retrieve an image by its filename and display it
     * 
     * @param ImageFilename The filename of the image to be retrieved
     * @return ResponseEntity with the image, or 404 if the image is not found
     * @throws MalformedURLException If the URL of the image is malformed
     */
    @GetMapping("/image/{ImageFilename}")
    public ResponseEntity<Resource> getImage(@PathVariable String ImageFilename) throws MalformedURLException {
        Path imagePath = PICTURE_PATH.resolve(ImageFilename); // Resolve the path of the image file
        Resource image = new UrlResource(imagePath.toUri()); // Load the image resource from the path

        // Check if the image exists and is readable
        if (image.exists() && image.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Set MIME type for the image
                    .body(image); // Return the image in the response
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if image is not found
        }
    }

    /**
     * Handles the POST request to delete a picture by its ID
     * 
     * @param id The ID of the picture to be deleted
     * @return "deleted_picture" view after the picture is deleted, or
     *         "picture_not_found" if not found
     */
    @PostMapping("/{id}/delete")
    public String deletePicture(@PathVariable long id) {
        Optional<Picture> picture = pictureService.findById(id); // Retrieve the picture by ID
        if (picture.isPresent()) {
            pictureService.delete(picture.get()); // Delete the picture from the database
            return "deleted_picture"; // Redirect to the "deleted_picture" view after deletion
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if picture does not exist
        }
    }

    /**
     * Handles the POST request to add a new comment to a picture
     * 
     * @param picId   The ID of the picture to which the comment will be added
     * @param comment The comment to be added to the picture
     * @return Redirect to the picture's page after the comment is saved
     */
    @PostMapping("/{picId}/comments/new")
    public String newComment(@PathVariable long picId, Comment comment) {
        Optional<Picture> picture = pictureService.findById(picId); // Retrieve the picture by ID
        if (picture.isPresent()) {
            Picture picture1 = picture.get(); // Get the picture object
            commentService.save(picture1, comment); // Save the new comment for the picture
            return "redirect:/picture/" + picId; // Redirect back to the picture's page
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if the picture does not exist
        }
    }

    /**
     * Handles the POST request to delete a comment from a picture
     * 
     * @param picId     The ID of the picture from which the comment will be deleted
     * @param commentId The ID of the comment to be deleted
     * @return Redirect to the picture's page after the comment is deleted
     */
    @PostMapping("/{picId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable long picId, @PathVariable long commentId) {
        Optional<Picture> picture = pictureService.findById(picId); // Retrieve the picture by ID
        if (picture.isPresent()) {
            Picture picture1 = picture.get(); // Get the picture object
            commentService.delete(commentId, picture1); // Delete the comment from the picture
            return "redirect:/picture/" + picId; // Redirect back to the picture's page
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if the picture does not exist
        }
    }

    /**
     * Handles the POST request to toggle like/unlike for a picture
     * 
     * @param picId  The ID of the picture to toggle like status
     * @param userId The ID of the user performing the like/unlike action
     * @return Redirect to the picture's page after the like status is toggled
     */
    @PostMapping("/{picId}/likeToggle")
    public String likePicture(@PathVariable Long picId, Long userId) {
        Optional<Picture> picture = pictureService.findById(picId); // Retrieve the picture by ID
        if (picture.isPresent()) {
            Picture picture1 = picture.get(); // Get the picture object
            userService.likeOrRemovePicture(userId, picture1); // Toggle the like status for the picture
            return "redirect:/picture/" + picId; // Redirect back to the picture's page
        } else {
            return "picture_not_found"; // Return "picture
        }
    }
}