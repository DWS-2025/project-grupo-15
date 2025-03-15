package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Username;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository class for managing User entities in memory.
 * Provides basic CRUD operations for User objects.
 */
public interface UsernameRepository extends JpaRepository<Username, Long> {
}
