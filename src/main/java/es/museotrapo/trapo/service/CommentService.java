package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;


    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public void save(Picture picture, Comment comment) {
        picture.getComments().add(comment);
        User currentUser = userService.getLoggedUser();
        comment.setAuthor(currentUser);
        currentUser.getComments().add(comment);
        commentRepository.save(comment);
    }

    public void delete(long commentId, Picture picture) {
        if(commentRepository.findById(commentId).isPresent()) {
            Comment comment = this.findById(commentId).get();
            picture.getComments().remove(comment);
            User author = comment.getAuthor();
            author.getComments().remove(comment);
            commentRepository.delete(comment);
        }
    }
}
