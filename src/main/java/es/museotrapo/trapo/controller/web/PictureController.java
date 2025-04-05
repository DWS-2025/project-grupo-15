package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.service.*;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/pictures") // Maps the controller to "/picture" endpoint
public class PictureController {

    @Autowired
    private PictureService pictureService; // Service to handle picture-related functionality

    @Autowired
    private ArtistService artistService; // Service to handle artist-related functionality

    @Autowired
    private UserService userService; // Service to handle user-related functionality

    /**
     * Handles the GET request to display all pictures
     *
     * @param model Model object to add attributes for the view
     * @return "pictures" view to render all pictures
     */
    @GetMapping("")
    public String getPictures(Model model, Pageable picturePage) {
        int pageSize = 3;
        picturePage = PageRequest.of(picturePage.getPageNumber(), pageSize);

        model.addAttribute("pictures", pictureService.getPictures(picturePage)); // Add all pictures to the model
        
        boolean hasPrev = picturePage.getPageNumber() >= 1;
    	boolean hasNext = (picturePage.getPageNumber() * picturePage.getPageSize()) < pictureService.count();

		model.addAttribute("hasPrev", hasPrev);
		model.addAttribute("prev", picturePage.getPageNumber() - 1);
		model.addAttribute("hasNext", hasNext);
		model.addAttribute("next", picturePage.getPageNumber() + 1);
        
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
        model.addAttribute("availableArtists", artistService.getArtists()); // Add available artists to the model
        return "new_picture"; // Return the "new_picture" view to display the form
    }

    @PostMapping("/new")
    public String newPicture(Model model,
                             PictureDTO pictureDTO, // The picture object to be saved
                             @RequestParam MultipartFile imageFileFull, // Image file associated with the picture
                             @RequestParam Long artistID) throws IOException { // ID of the artist
        pictureService.createPicture(pictureDTO, artistID, imageFileFull); // Save the picture in the database
        model.addAttribute("picture", pictureDTO); // Add the saved picture to the model

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
        PictureDTO picture = pictureService.getPicture(id); // Fetch the picture by ID
        if (picture != null) {
            model.addAttribute("picture", picture); // Add the picture to the model
            String likedPicture = userService.isPictureLiked(picture) ? "Dislike" : "Like"; // Check if the picture
                                                                                                 // is liked by the user
            model.addAttribute("likedPicture", likedPicture); // Add like status to the model
            model.addAttribute("picture", picture); // Add image path to the
                                                                                             // model
            return "show_picture"; // Return the "show_picture" view to display the picture details
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if picture does not exist
        }
    }

    /**
     * Handles the GET request to retrieve an image by its filename and display it
     *
     * @return ResponseEntity with the image, or 404 if the image is not found
     */
    @GetMapping("/{id}/{ImageFile}")
    public ResponseEntity<Resource> getImage(@PathVariable long id) throws SQLException {

        PictureDTO picture = pictureService.getPicture(id);

        // Check if the image exists and is readable
        if (picture.image() != null) {
            Resource file = pictureService.getPictureImage(id);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Set MIME type for the image
                    .body(file); // Return the image in the response
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if image is not found
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
        PictureDTO picture = pictureService.getPicture(id); // Retrieve the picture by ID
        if (picture != null) {
            pictureService.deletePicture(picture); // Delete the picture from the database
            return "deleted_picture"; // Redirect to the "deleted_picture" view after deletion
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if picture does not exist
        }
    }

    /**
     * Handles the POST request to add a new comment to a picture
     *
     * @param picId   The ID of the picture to which the comment will be added
     * @return Redirect to the picture's page after the comment is saved
     */
    @PostMapping("/{picId}/comments/new")
    public String newComment(@PathVariable long picId, CommentDTO commentDTO) {
        PictureDTO picture = pictureService.getPicture(picId); // Retrieve the picture by ID
        if (picture != null) {
            pictureService.addComment(commentDTO, picId);
            return "redirect:/pictures/" + picId; // Redirect back to the picture's page
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
        PictureDTO picture = pictureService.getPicture(picId); // Retrieve the picture by ID
        if (picture != null) {
            pictureService.removeComment(commentId, picId);
            return "redirect:/pictures/" + picId; // Redirect back to the picture's page
        } else {
            return "picture_not_found"; // Return "picture_not_found" view if the picture does not exist
        }
    }

    /**
     * Handles the POST request to toggle like/unlike for a picture
     *
     * @param picId  The ID of the picture to toggle like status
     * @return Redirect to the picture's page after the like status is toggled
     */
    @PostMapping("/{picId}/likeToggle")
    public String likePicture(@PathVariable Long picId) {
        PictureDTO picture = pictureService.getPicture(picId); // Retrieve the picture by ID
        if (picture != null) {
            userService.likeOrRemovePicture(picture);
            return "redirect:/pictures/" + picId; // Redirect back to the picture's page
        } else {
            return "picture_not_found"; // Return "picture
        }
    }
}