package es.museotrapo.trapo.controller.web;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.service.*;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller to manage picture-related actions.
 * Handles CRUD operations on pictures, displays picture data,
 * manages comments on pictures, and provides functionality for likes.
 */
@Controller
@RequestMapping("/pictures")
public class PictureController {

    @Autowired
    private PictureService pictureService; // Service for picture-related logic

    @Autowired
    private ArtistService artistService; // Service for artist-related logic

    @Autowired
    private UserService userService; // Service for user-related functionality

    /**
     * Displays all pictures with pagination.
     *
     * @param model       Model to add attributes for rendering in the view.
     * @param picturePage Page information to handle pagination.
     * @return The "pictures" view to display the paginated list of pictures.
     */
    @GetMapping("")
    public String getPictures(Model model, Pageable picturePage) {
        int pageSize = 3; // Define the number of pictures per page
        picturePage = PageRequest.of(picturePage.getPageNumber(), pageSize);

        // Add pictures and pagination controls to the model
        model.addAttribute("pictures", pictureService.getPictures(picturePage));
        boolean hasPrev = picturePage.getPageNumber() >= 1;
        boolean hasNext = (picturePage.getPageNumber() * picturePage.getPageSize()) < pictureService.count();
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", picturePage.getPageNumber() - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", picturePage.getPageNumber() + 1);

        return "pictures";
    }

    /**
     * Displays the form to create a new picture.
     *
     * @param model The model to populate with artists for the form.
     * @return The "new_picture" view.
     */
    @GetMapping("/new")
    public String newPicture(Model model) {
        model.addAttribute("availableArtists", artistService.getArtists()); // Add available artists for the dropdown
        return "new_picture"; // Return form for creating a new picture
    }

    /**
     * Creates a new picture with an associated file and artist.
     *
     * @param model           The model to add success information to.
     * @param pictureDTO      The picture details to create.
     * @param imageFileFull   The image file to associate with the picture.
     * @param artistID        The ID of the artist who created the picture.
     * @return The "saved_picture" view upon success.
     * @throws IOException If there's an issue while handling the file.
     */
    @PostMapping("/new")
    public String newPicture(Model model, PictureDTO pictureDTO,
                             @RequestParam MultipartFile imageFileFull,
                             @RequestParam Long artistID) throws IOException {
        pictureService.createPicture(pictureDTO, artistID, imageFileFull); // Save picture
        model.addAttribute("picture", pictureDTO); // Add the saved picture to the model
        return "saved_picture";
    }

    /**
     * Displays a single picture by its ID.
     *
     * @param model Model to add attributes for rendering.
     * @param id    The ID of the picture.
     * @return The "show_picture" view to display picture details or "picture_not_found" if not found.
     */
    @GetMapping("/{id}")
    public String getPicture(Model model, @PathVariable long id) {
        PictureDTO picture = pictureService.getPicture(id); // Fetch the picture
        if (picture != null) {
            model.addAttribute("picture", picture);
            String likedPicture = userService.isPictureLiked(picture) ? "Dislike" : "Like"; // Set Like/Dislike status
            model.addAttribute("likedPicture", likedPicture);
            return "show_picture"; // Display picture details
        } else {
            return "picture_not_found"; // If the picture does not exist
        }
    }

    /**
     * Retrieves an image file by its associated picture's ID.
     *
     * @param id The ID of the picture to fetch the image for.
     * @return ResponseEntity with the image file or 404 if not found.
     * @throws SQLException If a database issue occurs.
     */
    @GetMapping("/{id}/{ImageFile}")
    public ResponseEntity<Resource> getImage(@PathVariable long id) throws SQLException {
        PictureDTO picture = pictureService.getPicture(id); // Fetch the picture
        if (picture.image() != null) {
            Resource file = pictureService.getPictureImage(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Define content type as image
                    .body(file);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if image not found
        }
    }

    /**
     * Deletes a picture by its ID.
     *
     * @param id The ID of the picture to delete.
     * @return The "deleted_picture" view upon success, or "picture_not_found" if not found.
     */
    @PostMapping("/{id}/delete")
    public String deletePicture(@PathVariable long id) {
        PictureDTO picture = pictureService.getPicture(id); // Fetch the picture
        if (picture != null) {
            pictureService.deletePicture(picture); // Delete the picture
            return "deleted_picture";
        } else {
            return "picture_not_found"; // If picture does not exist
        }
    }

    /**
     * Creates a new comment for a specific picture.
     *
     * @param picId      The ID of the picture to comment on.
     * @param commentDTO The comment data to be added.
     * @return Redirect to the picture's page after adding the comment.
     */
    @PostMapping("/{picId}/comments/new")
    public String newComment(@PathVariable Long picId, CommentDTO commentDTO) {
        PictureDTO picture = pictureService.getPicture(picId); // Fetch the picture
        if (picture != null) {
            pictureService.addComment(commentDTO, picId); // Add the comment
            return "redirect:/pictures/" + picId;
        } else {
            return "picture_not_found"; // If picture does not exist
        }
    }

    /**
     * Deletes a comment by its ID from a specific picture.
     *
     * @param picId      The ID of the picture the comment belongs to.
     * @param commentId  The ID of the comment to delete.
     * @param authentication The user's authentication context.
     * @return Redirect to the picture's page or return "picture_not_found".
     */
    @PostMapping("/{picId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable long picId, @PathVariable long commentId, Authentication authentication) {
        PictureDTO picture = pictureService.getPicture(picId);
        if (picture != null) {
            pictureService.removeComment(commentId, picId, authentication); // Delete the comment
            return "redirect:/pictures/" + picId;
        } else {
            return "picture_not_found";
        }
    }

    /**
     * Toggles the like status for a specific picture.
     *
     * @param picId The ID of the picture.
     * @return Redirects to the picture's page after toggling like status.
     */
    @PostMapping("/{picId}/likeToggle")
    public String likePicture(@PathVariable Long picId) {
        PictureDTO picture = pictureService.getPicture(picId); // Fetch the picture
        if (picture != null) {
            userService.likeOrRemovePicture(picture); // Toggle like status
            return "redirect:/pictures/" + picId;
        } else {
            return "picture_not_found";
        }
    }
}