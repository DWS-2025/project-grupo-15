package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.ArtistMapper;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.dto.ArtistDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Collection;

/**
 * Service class for managing Artist entities.
 * Provides methods to interact with the ArtistRepository and PictureRepository to perform CRUD operations.
 */
@Service // Spring annotation indicating this is a service class
public class ArtistService {

    // Injecting ArtistRepository to interact with artist data in the database.
    @Autowired
    private ArtistRepository artistRepository;

    // Injecting ArtistMapper to convert between Artist domain objects and ArtistDTOs.
    @Autowired
    private ArtistMapper mapper;

    /**
     * Retrieves a paginated list of ArtistDTOs.
     *
     * @param pageable pagination information (e.g., page number, page size)
     * @return a Page of ArtistDTOs
     */
    public Page<ArtistDTO> getArtists(Pageable pageable) {
        // Fetches the paginated list of artists from the repository and converts them to DTOs
        Page<Artist> artistPage = artistRepository.findAll(pageable);
        return convertToDTOPage(artistPage);
    }

    /**
     * Retrieves a collection of all ArtistDTOs.
     *
     * @return a collection of ArtistDTOs
     */
    public Collection<ArtistDTO> getArtists() {
        // Converts all Artist entities to DTOs and returns them
        return toDTOs(artistRepository.findAll());
    }

    /**
     * Retrieves a single ArtistDTO by its ID.
     *
     * @param id the ID of the artist to retrieve
     * @return the ArtistDTO corresponding to the ID
     */
    public ArtistDTO getArtist(long id) {
        // Finds the artist by ID and converts it to a DTO
        return toDTO(artistRepository.findById(id).orElseThrow());
    }

    /**
     * Creates a new artist from the given ArtistDTO.
     *
     * @param artistDTO the ArtistDTO to create a new artist from
     * @return the created ArtistDTO
     */
    public ArtistDTO createArtist(ArtistDTO artistDTO) {
        // Converts the DTO to a domain object, saves it in the repository, and returns the saved DTO
        Artist artist = toDomain(artistDTO);
        artistRepository.save(artist);
        return toDTO(artist);
    }

    /**
     * Replaces an existing artist with a new artist, identified by the given ID.
     *
     * @param id               the ID of the artist to replace
     * @param updatedArtistDTO the updated ArtistDTO
     * @return the updated ArtistDTO
     * @throws NoSuchElementException if the artist does not exist
     */
    public ArtistDTO replaceArtist(long id, ArtistDTO updatedArtistDTO) {
        if (artistRepository.existsById(id)) {
            // Converts the updated DTO to a domain object, sets the ID, saves it, and returns the updated DTO
            Artist updatedArtist = toDomain(updatedArtistDTO);
            updatedArtist.setId(id);
            artistRepository.save(updatedArtist);
            return toDTO(updatedArtist);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Deletes an artist by ID.
     *
     * @param id the ID of the artist to delete
     * @return the ArtistDTO of the deleted artist
     */
    public ArtistDTO deleteArtist(long id) {
        Artist artist = artistRepository.findById(id).orElseThrow();
        ArtistDTO artistDTO = toDTO(artist);
        artistRepository.deleteById(id);
        return artistDTO;
    }

    /**
     * Converts a Page of Artist entities to a Page of ArtistDTOs.
     *
     * @param artistPage the Page of Artist entities
     * @return a Page of ArtistDTOs
     */
    public Page<ArtistDTO> convertToDTOPage(Page<Artist> artistPage) {
        return new PageImpl<>(
                artistPage.getContent().stream().map(mapper::toDTO).collect(Collectors.toList()),
                artistPage.getPageable(),
                artistPage.getTotalElements()
        );
    }

    /**
     * Returns the total count of Artist entities.
     *
     * @return the count of artists
     */
    public long count() {
        return artistRepository.count();
    }

    /**
     * Searches for artists based on the provided criteria: name, nickname, and birth date.
     * The search is case-insensitive.
     *
     * @param name      the name of the artist to search for (optional)
     * @param nickname  the nickname of the artist to search for (optional)
     * @param birthDate the birth date of the artist to search for (optional)
     * @param pageable  the pagination information
     * @return a paginated list of ArtistDTOs matching the search criteria
     */
    public Page<ArtistDTO> searchArtists(String name, String nickname, String birthDate, Pageable pageable) {
        Page<Artist> artistPage;

        // Checks the different combinations of search criteria and calls the corresponding repository method
        if (name != null && !name.isEmpty() && nickname != null && !nickname.isEmpty() && birthDate != null) {
            artistPage = artistRepository.findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(name, nickname, birthDate, pageable);
        } else if (name != null && !name.isEmpty() && nickname != null && !nickname.isEmpty()) {
            artistPage = artistRepository.findByNameContainingIgnoreCaseAndNicknameContainingIgnoreCase(name, nickname, pageable);
        } else if (name != null && !name.isEmpty() && birthDate != null) {
            artistPage = artistRepository.findByNameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(name, birthDate, pageable);
        } else if (nickname != null && !nickname.isEmpty() && birthDate != null) {
            artistPage = artistRepository.findByNicknameContainingIgnoreCaseAndBirthDateContainingIgnoreCase(nickname, birthDate, pageable);
        } else if (name != null && !name.isEmpty()) {
            artistPage = artistRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (nickname != null && !nickname.isEmpty()) {
            artistPage = artistRepository.findByNicknameContainingIgnoreCase(nickname, pageable);
        } else if (birthDate != null) {
            artistPage = artistRepository.findByBirthDateContainingIgnoreCase(birthDate, pageable);
        } else {
            artistPage = artistRepository.findAll(pageable);
        }

        // Converts the resulting Page of Artist entities to a Page of ArtistDTOs
        return convertToDTOPage(artistPage);
    }

    /**
     * Checks if an artist with the given ID exists.
     *
     * @param id the ID of the artist to check
     * @return true if the artist exists, false otherwise
     */
    public boolean existsById(long id) {
        return artistRepository.existsById(id);
    }

    // Helper methods for converting between domain objects and DTOs

    private ArtistDTO toDTO(Artist artist) {
        return mapper.toDTO(artist);
    }

    protected Artist toDomain(ArtistDTO artistDTO) {
        return mapper.toDomain(artistDTO);
    }

    private Collection<ArtistDTO> toDTOs(Collection<Artist> artists) {
        return mapper.toDTOs(artists);
    }
}
