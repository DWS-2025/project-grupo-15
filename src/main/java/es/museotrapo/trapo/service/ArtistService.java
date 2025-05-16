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

    @Autowired
    private SanitizeService sanitizeService;

    private final String biographyDir = "biographies";

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

    public Page<ArtistDTO> getArtists(Example<Artist> example, Pageable pageable) {
        // Fetches the paginated list of artists from the repository and converts them to DTOs
        Page<Artist> artistPage = artistRepository.findAll(example, pageable);
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
     * Saves the biography file for a specific artist by validating the file,
     * sanitizing its content and name, and ensuring it is safely stored in the designated directory.
     *
     * @param id   The unique identifier of the artist.
     * @param file The uploaded file containing the biography.
     * @throws IOException              If an I/O error occurs while processing the file.
     * @throws RuntimeException         If the artist is not found or the file cannot be safely saved.
     * @throws IllegalArgumentException If the file name is invalid or the file cannot be processed.
     * @throws SecurityException        If a path traversal attempt or unsafe file path is detected.
     */
    public void saveBiography(Long id, MultipartFile file) throws IOException {
        // Retrieve artist from the database using the provided ID.
        // Throws an exception if the artist does not exist.
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        var folder = Files.createTempDirectory("MYAPP");
        var fileTemp = folder.resolve("asdasdasd");
        file.transferTo(fileTemp);

        // Validate the file's content and extension to ensure it is a valid and secure file.
        sanitizeService.validateFileExtensionAndContent(fileTemp, file.getOriginalFilename(), file.getInputStream());

        // Create the biography directory if it doesn't exist.
        File directory = new File(biographyDir);
        if (!directory.exists()) {
            // mkdirs is used to create the directory and any non-existing parent directories.
            directory.mkdirs();
        }

        // Sanitize the file name to prevent unsafe or invalid characters (e.g., ../ or special characters).
        String originalName = sanitizeService.sanitizeFileName(file.getOriginalFilename());
        if (originalName == null || originalName.isEmpty()) {
            throw new IllegalArgumentException("The file does not have a valid name");
        }

        // Resolve the destination file path and ensure it is canonical (safe and absolute).
        File destinationFile = new File(biographyDir, originalName).getCanonicalFile();

        // Check that the resolved file path is within the intended directory (prevent path traversal attacks).
        if (!destinationFile.getPath().startsWith(directory.getCanonicalPath())) {
            throw new SecurityException("Invalid file path. File must reside in the biography directory");
        }

        // Check if a file with the same name already exists to prevent overwriting.
        if (destinationFile.exists()) {
            throw new RuntimeException("There is already a file with the same name: " + originalName);
        }

        // Save the file to the destination directory.
        file.transferTo(destinationFile);

        // Save the file reference (absolute path) in the artist's biography field.
        artist.setBiography(biographyDir + "/" + originalName);

        // Update the artist entity in the database.
        artistRepository.save(artist);
    }

    public ResponseEntity<Resource> getBiographyResponse(Long id) throws IOException {
        Artist artist = artistRepository.findById(id).orElseThrow();
        // Construir la ruta al archivo
        Path path = Paths.get(artist.getBiography());

        // Validar si el archivo existe
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Biography not found for artist with ID: " + id);
        }

        // Crear un recurso a partir del archivo
        Resource pdfResource = new UrlResource(path.toUri());

        // Construir la respuesta HTTP con el recurso
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=biography_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
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
