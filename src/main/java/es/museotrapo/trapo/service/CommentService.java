package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.dto.CommentMapper;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.CommentRepository;
import es.museotrapo.trapo.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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
     * @param picId the ID of the picture associated with the comment
     * @return the deleted CommentDTO
     * @throws NoSuchElementException if the comment or picture is not found
     */
    public CommentDTO deleteComment(long commentId, Long picId) {
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

    // Helper method to convert a single Comment entity to CommentDTO
    protected CommentDTO toDTO(Comment comment) {
        return mapper.toDTO(comment);
    }

    // Helper method to convert a CommentDTO to a Comment domain object
    protected Comment toDomain(CommentDTO commentDTO) {
        return mapper.toDomain(commentDTO);
    }

    // Helper method to convert a collection of Comment entities to CommentDTOs
    private Collection<CommentDTO> toDTOs(Collection<Comment> comments) {
        return mapper.toDTOs(comments);
    }
}
