package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository class for managing Picture entities in memory.
 * Provides basic CRUD operations for Picture objects.
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {
}
