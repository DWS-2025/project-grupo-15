package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing Comment entities.
 * Provides methods to interact with CommentRepository and handle
 * comment-related operations.
 */
@Service
public class CommentService {

    // Injecting UserService to retrieve the currently logged-in user.
    @Autowired
    private UsernameService usernameService;

    // Injecting CommentRepository to manage comment data.
    @Autowired
    private CommentRepository commentRepository;

    /**
     * Finds a comment by its ID.
     *
     * @param id The ID of the comment.
     * @return An Optional containing the comment if found, or empty if not found.
     */
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    /**
     * Saves a new comment associated with a picture and the currently logged-in
     * user.
     * The comment is added to both the picture's and the user's comment list.
     *
     * @param picture The picture to which the comment is added.
     * @param comment The comment to save.
     */
    public void save(Picture picture, Comment comment) {
        picture.getComments().add(comment);// Add the comment to the picture's comment list
        User currentUser = usernameService.getLoggedUser(); // Retrieve the currently logged-in user
        comment.setAuthor(currentUser);// Set the comment's author as the logged-in user
        currentUser.getComments().add(comment);// Add the comment to the user's list of comments
        commentRepository.save(comment);// Save the comment in the repository
    }

    /**
     * Deletes a comment by its ID, removing it from both the picture and the
     * author's comment list.
     *
     * @param commentId The ID of the comment to delete.
     * @param picture   The picture from which the comment is removed.
     */
    public void delete(long commentId, Picture picture) {
        // Check if the comment exists in the repository
        if (commentRepository.findById(commentId).isPresent()) {
            Comment comment = this.findById(commentId).get();// Retrieve the comment from the repository
            picture.getComments().remove(comment);// Remove the comment from the picture's comment list
            User author = comment.getAuthor();// Remove the comment from the author's list of comments
            author.getComments().remove(comment);
            commentRepository.delete(comment);// Delete the comment from the repository
        }
    }
}