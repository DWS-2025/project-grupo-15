package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository class for managing Artist entities in memory.
 */
@Component
public class ArtistRepository {

    // Atomic counter to generate unique IDs for new artists.
    private AtomicLong nextId = new AtomicLong(1L);

    // Thread-safe map to store artists using their ID as the key.
    private ConcurrentHashMap<Long, Artist> artistMap = new ConcurrentHashMap<>();

    /**
     * Retrieves all artists stored in the repository.
     *
     * @return List of all artists.
     */
    public List<Artist> findAll() {
        return artistMap.values().stream().toList();
    }

    /**
     * Finds an artist by their ID.
     *
     * @param id The ID of the artist.
     * @return An Optional containing the artist if found, or empty if not found.
     */
    public Optional<Artist> findById(long id) {
        return Optional.ofNullable(artistMap.get(id));
    }

    /**
     * Saves a new artist or updates an existing one.
     * If the artist has no ID (0), assigns a new unique ID.
     *
     * @param artist The artist to save.
     */
    public void save(Artist artist) {
        long id = artist.getId();
        if (id == 0) {
            id = nextId.incrementAndGet();// Assign a new ID if not already set.
            artist.setId(id);
        }
        artistMap.put(id, artist);
    }

    /**
     * Deletes an artist from the repository by their ID.
     *
     * @param id The ID of the artist to remove.
     */
    public void deleteById(long id) {
        artistMap.remove(id);
    }

    /**
     * Adds a picture to an artist's collection.
     *
     * @param picture The picture to add.
     * @param id      The ID of the artist to whom the picture belongs.
     */
    public void addPicture(Picture picture, Long id) {
        List<Picture> pictures;
        pictures = artistMap.get(id).getPaintedPictures();
        pictures.add(picture);
        artistMap.get(id).setPaintedPictures(pictures);
    }

    /**
     * Removes a picture from an artist's collection.
     * If the picture has an associated author, it removes the picture from that
     * author's collection.
     *
     * @param picture The picture to remove.
     * @param id      The ID of the artist from whom the picture should be removed.
     */
    public void deletePicture(Picture picture, Long id) {
        if (picture.getAuthor() != null) {
            picture.getAuthor().getPaintedPictures().remove(picture);
        }
    }
}

