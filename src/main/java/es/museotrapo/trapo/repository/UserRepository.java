package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository class for managing User entities in memory.
 * Provides basic CRUD operations for User objects.
 */
@Component
public class UserRepository {

    // Atomic counter to generate unique IDs for new users in a thread-safe manner.
    private AtomicLong nextId = new AtomicLong(1L);

    // Thread-safe map to store users using their ID as the key.
    private ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();

    /**
     * Retrieves all users stored in the repository.
     *
     * @return A list of all users.
     */
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    /**
     * Saves a new user or updates an existing one.
     * If the user has no ID (0), assigns a new unique ID.
     *
     * @param user The user to save.
     */
    public void save(User user) {
        long id = user.getId();
        if (id == 0) {
            id = nextId.incrementAndGet();// Assign a new ID if not already set.
            user.setId(id);
        }
        userMap.put(id, user);
    }
}
