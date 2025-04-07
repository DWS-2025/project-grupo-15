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

    /**
     * Retrieves a paginated list of Artist entities whose names contain the provided string (case-insensitive).
     *
     * @param name     the name substring to search for
     * @param pageable the pagination information
     * @return a page of Artist entities whose names contain the given substring
     */
    Page<Artist> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Retrieves a paginated list of Artist entities whose nicknames contain the provided string (case-insensitive).
     *
     * @param nickname the nickname substring to search for
     * @param pageable the pagination information
     * @return a page of Artist entities whose nicknames contain the given substring
     */
    Page<Artist> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);

    /**
     * Retrieves a paginated list of Artist entities whose birthdates contain the provided string (case-insensitive).
     *
     * @param birthDate the birthdate substring to search for
     * @param pageable  the pagination information
     * @return a page of Artist entities whose birthdates contain the given substring
     */
    Page<Artist> findByBirthDateContainingIgnoreCase(String birthDate, Pageable pageable);

    /**
     * Retrieves a paginated list of Artist entities whose nicknames and birthdates contain the provided substrings (case-insensitive).
     *
     * @param nickname  the nickname substring to search for
     * @param birthDate the birthdate substring to search for
     * @param pageable  the pagination information
     * @return a page of Artist entities matching the nickname and birthdate criteria
     */
    Page<Artist> findByNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(String nickname, String birthDate, Pageable pageable);

    /**
     * Retrieves a paginated list of Artist entities whose names, nicknames, and birthdates match the provided substrings (case-insensitive).
     *
     * @param name     the name substring to search for
     * @param nickname the nickname substring to search for
     * @param pageable the pagination information
     * @return a page of Artist entities matching the name, nickname, and birthdate criteria
     */
    Page<Artist> findByNameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(String name, String nickname, Pageable pageable);

    /**
     * Retrieves a paginated list of Artist entities whose names, nicknames, and birthdates contain the provided substrings (case-insensitive).
     *
     * @param name      the name substring to search for
     * @param nickname  the nickname substring to search for
     * @param birthDate the birthdate substring to search for
     * @param pageable  the pagination information
     * @return a page of Artist entities matching all three criteria
     */
    Page<Artist> findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCase(String name, String nickname, Pageable pageable);

    /**
     * Retrieves a paginated list of Artist entities that match the provided substrings for name, nickname, and birthdate (case-insensitive).
     *
     * @param name      the name substring to search for
     * @param nickname  the nickname substring to search for
     * @param birthDate the birthdate substring to search for
     * @param pageable  the pagination information
     * @return a page of Artist entities matching all the provided search criteria
     */
    Page<Artist> findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(String name, String nickname,
                                                                                                               String birthDate, Pageable pageable);
}

