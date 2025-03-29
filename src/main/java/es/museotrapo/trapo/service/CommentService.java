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

/**
 * Service class for managing Comment entities.
 * Provides methods to interact with CommentRepository and handle
 * comment-related operations.
 */
@Service
public class CommentService {

    // Injecting CommentRepository to manage comment data.
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper mapper;

    @Autowired
    private PictureRepository pictureRepository;

    /**
     * Finds a comment by its ID.
     *
     * @param id The ID of the comment.
     * @return An Optional containing the comment if found, or empty if not found.
     */
    public CommentDTO getComment(long id) {
        return toDTO(commentRepository.findById(id).orElseThrow(null));
    }

    public CommentDTO deleteComment(long commentId, Long picId) {

        Picture picture = pictureRepository.findById(picId).orElseThrow(null);
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