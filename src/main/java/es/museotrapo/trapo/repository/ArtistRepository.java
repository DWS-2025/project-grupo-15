package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository class for managing Artist entities in memory.
 */
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @NonNull
    Page<Artist> findAll(Pageable pageable);

    Page<Artist> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Artist> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
    Page<Artist> findByBirthDateContainingIgnoreCase(String birthDate, Pageable pageable);

    Page<Artist> findByNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(String nickname, String birthDate, Pageable pageable);
    Page<Artist> findByNameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(String name, String nickname, Pageable pageable);
    Page<Artist> findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCase(String name, String nickname, Pageable pageable);

    Page<Artist> findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(String name, String nickname,
            String birthDate, Pageable pageable);
}

