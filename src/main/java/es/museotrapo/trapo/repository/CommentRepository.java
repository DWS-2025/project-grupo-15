package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * CommentRepository interface for managing Comment entities.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}


