package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.CommentMapper;
import es.museotrapo.trapo.exceptions.UnauthorizedCommentDeleteException;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.CommentRepository;
import es.museotrapo.trapo.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Service class for managing `Comment` entities.
 * Encapsulates business logic related to comments, such as retrieval, deletion, and interaction with related entities.
 */
@Service
public class CommentService {

    // Injecting `CommentRepository` to interact with comments in the database.
    @Autowired
    private CommentRepository commentRepository;

    // Injecting `CommentMapper` to convert between `Comment` domain objects and `CommentDTO`.
    @Autowired
    private CommentMapper mapper;

    // Injecting `PictureRepository` to access pictures for comment-related operations.
    @Autowired
    private PictureRepository pictureRepository;

    /**
     * Retrieves a comment by its ID and converts it to a `CommentDTO`.
     *
     * @param id ID of the comment to retrieve.
     * @return The corresponding `CommentDTO`.
     * @throws NoSuchElementException If the comment does not exist.
     */
    public CommentDTO getComment(long id) {
        // Find the comment in the repository and convert it to a DTO.
        Comment comment = commentRepository.findById(id).orElseThrow();
        return toDTO(comment);
    }

    /**
     * Deletes a specific comment by its ID from a specific picture.
     * Ensures that only the comment's author or an admin can delete the comment.
     *
     * @param commentId The ID of the comment to delete.
     * @param picId     The ID of the associated picture.
     * @param authentication The logged-in user's authentication data.
     * @return The deleted `CommentDTO`.
     * @throws RuntimeException If the comment or picture is not found in the database.
     * @throws UnauthorizedCommentDeleteException If the user is not allowed to delete the comment.
     */
    public CommentDTO deleteComment(Long commentId, Long picId, Authentication authentication) {
        // Find the relevant picture and comment entities.
        Picture picture = pictureRepository.findById(picId)
                .orElseThrow(() -> new RuntimeException("Picture not found."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found."));

        // Extract the username of the logged-in user from the authentication object.
        String username = authentication.getName();

        // Check whether the user has admin privileges.
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // Verify if the logged-in user has permission to delete the comment.
        if (!comment.getAuthor().getName().equals(username) && !isAdmin) {
            // If neither the author nor an admin, throw an exception.
            throw new UnauthorizedCommentDeleteException("You can't delete other users' comments. (" + username
                    + " tried to delete comment " + commentId + " from picture " + picId + " by "
                    + comment.getAuthor().getName() + ")");
        }

        // Remove the comment from associated entities.
        picture.getComments().remove(comment); // Remove comment from the picture.
        User author = comment.getAuthor();
        author.getComments().remove(comment);  // Remove comment from the author's list.

        // Delete the comment from the database.
        commentRepository.delete(comment);

        // Return the deleted comment as a DTO.
        return toDTO(comment);
    }

    /**
     * An alternative or helper method for deleting a comment without authentication checks.
     *
     * @param commentId ID of the comment to delete.
     * @param picId     ID of the picture associated with the comment.
     * @return The deleted `CommentDTO`.
     * @throws RuntimeException If the picture or comment does not exist.
     */
    public CommentDTO deleteCommentHelp(Long commentId, Long picId) {
        // Find the relevant picture and comment entities.
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // Remove references to the comment from other entities.
        picture.getComments().remove(comment); // Remove from picture.
        User author = comment.getAuthor();
        author.getComments().remove(comment);  // Remove from author's list.

        // Delete the comment from the database.
        commentRepository.delete(comment);

        // Return the deleted comment as a DTO.
        return toDTO(comment);
    }

    /**
     * Adds a new comment to the database.
     *
     * @param comment The comment to be added.
     */
    public void addComment(Comment comment) {
        // Save the comment entity to the database.
        commentRepository.save(comment);
    }

    /* Conversion Helper Methods */

    /**
     * Converts a `Comment` domain object to a `CommentDTO`.
     *
     * @param comment The domain object.
     * @return The `CommentDTO`.
     */
    protected CommentDTO toDTO(Comment comment) {
        return mapper.toDTO(comment);
    }

    /**
     * Converts a `CommentDTO` to a `Comment` domain object.
     *
     * @param commentDTO The DTO.
     * @return The domain object.
     */
    protected Comment toDomain(CommentDTO commentDTO) {
        return mapper.toDomain(commentDTO);
    }

    /**
     * Converts a collection of `Comment` domain objects to `CommentDTOs`.
     *
     * @param comments The collection of domain objects.
     * @return The collection of `CommentDTOs`.
     */
    protected Collection<CommentDTO> toDTOs(Collection<Comment> comments) {
        return mapper.toDTOs(comments);
    }
}