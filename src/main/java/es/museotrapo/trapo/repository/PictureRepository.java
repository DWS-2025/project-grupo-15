package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Picture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

/**
 * Repository interface for managing Picture entities. Extends JpaRepository
 * to provide CRUD operations and custom query methods for Picture entities.
 * This repository offers basic CRUD functionalities for Picture objects in memory.
 */
public interface PictureRepository extends JpaRepository<Picture, Long> {

    /**
     * Retrieves all Picture entities in a paginated manner.
     *
     * @param pageable the pagination information (e.g., page number, page size)
     * @return a page of Picture entities
     */
    @NonNull
    Page<Picture> findAll(Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM picture WHERE id = :id", nativeQuery = true)
    void deleteByIdCustom(@Param("id") Long id);

}

