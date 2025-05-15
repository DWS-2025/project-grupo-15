package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for managing Artist entities. Extends JpaRepository
 * to provide CRUD operations and custom query methods for Artist entities.
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Retrieves all Artist entities in a paginated manner.
     *
     * @param pageable the pagination information (e.g., page number, page size)
     * @return a page of Artist entities
     */
    @NonNull
    Page<Artist> findAll(Pageable pageable);

}

