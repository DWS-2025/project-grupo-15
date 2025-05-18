package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.ArtistMapper;
import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.dto.ArtistDTO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Collection;
import org.springframework.http.MediaType;

/**
 * Service class for managing `Artist` entities.
 * Contains business logic to interact with the database, validate inputs, and process entity-related data.
 */
@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository; // Repository for database operations on `Artist` entities.

    @Autowired
    private ArtistMapper mapper; // Mapper for converting between `Artist` and `ArtistDTO`.

    @Autowired
    private SanitizeService sanitizeService; // Service for sanitizing and validating files.

    private final String biographyDir = "biographies"; // Directory to store biography files.

    /* CRUD Operations */

    /**
     * Retrieves all artists as a paginated list of `ArtistDTO`.
     *
     * @param pageable Pagination information such as page size and number.
     * @return Paginated list of `ArtistDTO`.
     */
    public Page<ArtistDTO> getArtists(Pageable pageable) {
        Page<Artist> artistPage = artistRepository.findAll(pageable);
        return convertToDTOPage(artistPage);
    }

    /**
     * Fetches the list of artists matching a given example as a paginated response.
     *
     * @param example Filter criteria represented by an example `Artist` object.
     * @param pageable Pagination information.
     * @return Paginated list of `ArtistDTO`.
     */
    public Page<ArtistDTO> getArtists(Example<Artist> example, Pageable pageable) {
        Page<Artist> artistPage = artistRepository.findAll(example, pageable);
        return convertToDTOPage(artistPage);
    }

    /**
     * Retrieves all artists as a non-paginated collection of `ArtistDTO`.
     *
     * @return List of `ArtistDTO`.
     */
    public Collection<ArtistDTO> getArtists() {
        return toDTOs(artistRepository.findAll());
    }

    /**
     * Retrieves a single `Artist` entity by its ID and converts it to `ArtistDTO`.
     *
     * @param id ID of the artist.
     * @return `ArtistDTO` object.
     * @throws NoSuchElementException If the artist with the given ID does not exist.
     */
    public ArtistDTO getArtist(long id) {
        return toDTO(artistRepository.findById(id).orElseThrow());
    }

    /**
     * Creates a new artist in the database.
     *
     * @param artistDTO Object with the artist's details.
     * @return The created `ArtistDTO`.
     */
    public ArtistDTO createArtist(ArtistDTO artistDTO) {
        Artist artist = toDomain(artistDTO);
        artistRepository.save(artist);
        return toDTO(artist);
    }

    /**
     * Updates/overwrites an existing artist by its ID.
     *
     * @param id ID of the artist to update.
     * @param updatedArtistDTO Updated artist details.
     * @return Updated `ArtistDTO`.
     * @throws NoSuchElementException If the artist does not exist.
     */
    public ArtistDTO replaceArtist(long id, ArtistDTO updatedArtistDTO) {
        if (artistRepository.existsById(id)) {
            Artist updatedArtist = toDomain(updatedArtistDTO);
            updatedArtist.setId(id); // Ensure the ID matches the existing entity.
            artistRepository.save(updatedArtist);
            return toDTO(updatedArtist);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Deletes an artist by ID and returns the deleted `ArtistDTO`.
     *
     * @param id ID of the artist to delete.
     * @return `ArtistDTO` of the deleted artist.
     * @throws NoSuchElementException If the artist does not exist.
     */
    public ArtistDTO deleteArtist(long id) {
        Artist artist = artistRepository.findById(id).orElseThrow();
        ArtistDTO artistDTO = toDTO(artist);
        artistRepository.deleteById(id);
        return artistDTO;
    }

    /**
     * Checks if an artist exists by ID.
     *
     * @param id ID of the artist.
     * @return `true` if the artist exists, otherwise `false`.
     */
    public boolean existsById(long id) {
        return artistRepository.existsById(id);
    }

    /* Biography Management */

    /**
     * Saves an artist's biography file.
     *
     * - Validates the file's content and name.
     * - Ensures the file path is secure and stores the file in the biography directory.
     * - Updates the artist's biography path in the database.
     *
     * @param id    ID of the artist for whom the biography is saved.
     * @param file  Biography file uploaded by the user.
     * @throws IOException If there's an error processing the file.
     */
    public void saveBiography(Long id, MultipartFile file) throws IOException {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        sanitizeService.validateFileExtensionAndContent(file.getOriginalFilename(), file.getInputStream());

        File directory = new File(biographyDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist.
        }

        String sanitizedFileName = sanitizeService.sanitizeFileName(file.getOriginalFilename());
        if (sanitizedFileName == null || sanitizedFileName.isEmpty()) {
            throw new IllegalArgumentException("The file does not have a valid name.");
        }

        File destinationFile = new File(biographyDir, sanitizedFileName).getCanonicalFile();
        if (!destinationFile.getPath().startsWith(directory.getCanonicalPath())) {
            throw new SecurityException("Invalid file path. File must reside in the biography directory.");
        }

        if (destinationFile.exists()) {
            throw new RuntimeException("A file with the same name already exists.");
        }

        file.transferTo(destinationFile);
        artist.setBiography(biographyDir + "/" + sanitizedFileName);

        artistRepository.save(artist); // Update the artist with the new biography path.
    }

    /**
     * Serves the biography file of the given artist as an HTTP response.
     *
     * @param id ID of the artist.
     * @return HTTP response containing the biography file as a downloadable resource.
     * @throws IOException If the file cannot be found or accessed.
     */
    public ResponseEntity<Resource> getBiographyResponse(Long id) throws IOException {
        Artist artist = artistRepository.findById(id).orElseThrow();
        Path path = Paths.get(artist.getBiography());

        if (!Files.exists(path)) {
            throw new FileNotFoundException("Biography not found for artist with ID: " + id);
        }

        Resource pdfResource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=biography_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
    }

    /* Helper Methods for Conversions */

    protected ArtistDTO toDTO(Artist artist) {
        return mapper.toDTO(artist);
    }

    protected Artist toDomain(ArtistDTO artistDTO) {
        return mapper.toDomain(artistDTO);
    }

    protected Collection<ArtistDTO> toDTOs(Collection<Artist> artists) {
        return mapper.toDTOs(artists);
    }

    public Page<ArtistDTO> convertToDTOPage(Page<Artist> artistPage) {
        return new PageImpl<>(
                artistPage.getContent().stream().map(mapper::toDTO).collect(Collectors.toList()),
                artistPage.getPageable(),
                artistPage.getTotalElements()
        );
    }

    /**
     * Counts the total number of artists in the database.
     *
     * @return Total count of artist entities.
     */
    public long count() {
        return artistRepository.count();
    }
}