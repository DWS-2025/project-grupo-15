package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}


