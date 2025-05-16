package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository class for managing User entities in memory.
 * Provides basic CRUD operations for User objects.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
