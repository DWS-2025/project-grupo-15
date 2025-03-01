package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Comment;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository class for managing Comment entities in memory.
 * This acts as a simple in-memory database for storing and retrieving comments.
 */
@Component
public class CommentRepository {

    // Atomic counter to generate unique IDs for new comments in a thread-safe
    // manner.
    private AtomicLong nextId = new AtomicLong(1L);

    // Thread-safe map to store comments using their ID as the key.
    private ConcurrentHashMap<Long, Comment> comments = new ConcurrentHashMap<>();

    /**
     * Finds a comment by its ID.
     *
     * @param id The ID of the comment to retrieve.
     * @return An Optional containing the comment if found, or empty if not found.
     */
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(comments.get(id));
    }

    /**
     * Saves a new comment or updates an existing one.
     * If the comment does not have an ID (0), assigns a new unique ID.
     *
     * @param comment The comment to save.
     */
    public void save(Comment comment) {
        long id = comment.getId();
        if (id == 0) {
            id = nextId.getAndIncrement();// Assign a new ID if not already set.
            comment.setId(id);
        }
        comments.put(id, comment);
    }

    /**
     * Deletes a comment from the repository.
     *
     * @param comment The comment to remove.
     */
    public void delete(Comment comment) {
        comments.remove(comment.getId());
    }
}


