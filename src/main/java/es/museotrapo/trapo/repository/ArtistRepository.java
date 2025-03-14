package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository class for managing Artist entities in memory.
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}

