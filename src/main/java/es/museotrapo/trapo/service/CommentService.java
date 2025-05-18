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


/**
 * Service class for managing Comment entities.
 * Provides methods to interact with CommentRepository and handle
 * comment-related operations.
 */
@Service // Spring annotation indicating this is a service class
public class CommentService {

    // Injecting CommentRepository to manage comment data in the database.
    @Autowired
    private CommentRepository commentRepository;

    // Injecting CommentMapper to convert between Comment domain objects and CommentDTOs.
    @Autowired
    private CommentMapper mapper;

    // Injecting PictureRepository to interact with picture data in the database.
    @Autowired
    private PictureRepository pictureRepository;

    /**
     * Finds a comment by its ID and converts it to a DTO.
     *
     * @param id The ID of the comment.
     * @return the CommentDTO corresponding to the given ID.
     * @throws NoSuchElementException if the comment is not found.
     */
    public CommentDTO getComment(long id) {
        // Retrieves the comment by ID, converts it to DTO and returns it
        return toDTO(commentRepository.findById(id).orElseThrow());
    }

    /**
     * Deletes a comment from a picture and from the comment repository.
     * Removes the comment from the associated picture and author.
     *
     * @param commentId the ID of the comment to delete
     * @param picId     the ID of the picture associated with the comment
     * @return the deleted CommentDTO
     */
    public CommentDTO deleteComment(Long commentId, Long picId, Authentication authentication) {
        // Fetch the Picture and Comment entities by their respective IDs
        Picture picture = pictureRepository.findById(picId)
                .orElseThrow(() -> new RuntimeException("Picture not found."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found."));
        String username = authentication.getName();

        // Check if the user is an admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        // Check if the user has permission to delete the comment
        if (!comment.getAuthor().getName().equals(username) && !isAdmin) {
            // Throw exception and ensure no further lines are executed
            throw new UnauthorizedCommentDeleteException("You can't delete other users' comments. (" + username + " tried to delete comment "
                    + commentId + " from picture " + picId + " by "
                    + comment.getAuthor().getName() + ")");
        }

        // Actual deletion process
        picture.getComments().remove(comment);  // Remove from picture
        User author = comment.getAuthor();
        author.getComments().remove(comment);   // Remove from author
        commentRepository.delete(comment);      // Delete from database

        // Return the deleted comment as a DTO
        return toDTO(comment);
    }

    /**
     * Deletes a comment from a picture and from the comment repository.
     * Removes the comment from the associated picture and author.
     *
     * @param commentId the ID of the comment to delete
     * @param picId     the ID of the picture associated with the comment
     * @return the deleted CommentDTO
     */
    public CommentDTO deleteCommentHelp(Long commentId, Long picId) {
        // Fetch the Picture and Comment entities by their respective IDs
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // Remove the comment from the picture's list of comments
        picture.getComments().remove(comment);

        // Remove the comment from the author's list of comments
        User author = comment.getAuthor();
        author.getComments().remove(comment);

        // Delete the comment from the repository
        commentRepository.delete(comment);

        // Return the deleted comment as a DTO
        return toDTO(comment);
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    // Helper method to convert a single Comment entity to CommentDTO
    protected CommentDTO toDTO(Comment comment) {
        return mapper.toDTO(comment);
    }

    // Helper method to convert a CommentDTO to a Comment domain object
    protected Comment toDomain(CommentDTO commentDTO) {
        return mapper.toDomain(commentDTO);
    }

    // Helper method to convert a collection of Comment entities to CommentDTOs
    protected Collection<CommentDTO> toDTOs(Collection<Comment> comments) {
        return mapper.toDTOs(comments);
    }
}
