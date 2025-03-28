package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.dto.CommentMapper;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

    @Autowired
    private PictureService pictureService;

    @Autowired
    private CommentMapper mapper;

    /**
     * Finds a comment by its ID.
     *
     * @param id The ID of the comment.
     * @return An Optional containing the comment if found, or empty if not found.
     */
    public CommentDTO getComment(long id) {
        return toDTO(commentRepository.findById(id).orElseThrow(null));
    }

    public CommentDTO createComment(PictureDTO pictureDTO, CommentDTO commentDTO) {
        Picture picture = pictureService.toDomain(pictureDTO);
        Comment comment = toDomain(commentDTO);
        picture.getComments().add(comment);// Add the comment to the picture's comment list
        User currentUser = usernameService.getLoggedUser(); // Retrieve the currently logged-in user
        comment.setAuthor(currentUser);// Set the comment's author as the logged-in user
        currentUser.getComments().add(comment);// Add the comment to the user's list of comments
        commentRepository.save(comment);// Save the comment in the repository
        return toDTO(comment);
    }

    public CommentDTO deleteComment(long commentId, PictureDTO pictureDTO) {

        Picture picture = pictureService.toDomain(pictureDTO);
        // Check if the comment exists in the repository
        if (commentRepository.findById(commentId).isPresent()) {
            Comment comment = toDomain(this.getComment(commentId));// Retrieve the comment from the repository
            picture.getComments().remove(comment);// Remove the comment from the picture's comment list
            User author = comment.getAuthor();// Remove the comment from the author's list of comments
            author.getComments().remove(comment);
            commentRepository.delete(comment);// Delete the comment from the repository
            return toDTO(comment);
        }
        return null;
    }

    private CommentDTO toDTO(Comment comment){
		return mapper.toDTO(comment);
	}

	protected Comment toDomain(CommentDTO commentDTO){
		return mapper.toDomain(commentDTO);
	}

	private Collection<CommentDTO> toDTOs(Collection<Comment> comments){
		return mapper.toDTOs(comments);
	}
}