package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository class for managing Picture entities in memory.
 * Provides basic CRUD operations for Picture objects.
 */
@Component
public class PictureRepository {

    // Atomic counter to generate unique IDs for new pictures in a thread-safe
    // manner.
    private AtomicLong nextId = new AtomicLong(1L);

    // Thread-safe map to store pictures using their ID as the key.
    private ConcurrentHashMap<Long, Picture> pictureMap = new ConcurrentHashMap<>();

    /**
     * Retrieves all pictures stored in the repository.
     *
     * @return A list of all pictures.
     */
    public List<Picture> findAll() {
        return pictureMap.values().stream().toList();
    }

    /**
     * Finds a picture by its ID.
     *
     * @param id The ID of the picture.
     * @return An Optional containing the picture if found, or empty if not found.
     */
    public Optional<Picture> findById(long id) {
        return Optional.ofNullable(pictureMap.get(id));
    }

    /**
     * Saves a new picture or updates an existing one.
     * If the picture has no ID (0), assigns a new unique ID.
     *
     * @param picture The picture to save.
     */
    public void save(Picture picture) {
        long id = picture.getId();
        if (id == 0) {
            id = nextId.incrementAndGet();// Assign a new ID if not already set.
            picture.setId(id);
        }
        pictureMap.put(id, picture);
    }

    /**
     * Deletes a picture from the repository by its ID.
     *
     * @param id The ID of the picture to remove.
     */
    public void deleteById(long id) {
        pictureMap.remove(id);
    }

    /**
     * Removes the association between an artist and all their pictures.
     * This sets the author of each picture to null, effectively dissociating the
     * artist.
     *
     * @param artist The artist whose pictures should be updated.
     */
    public void deleteArtistInPicture(Artist artist) {
        for (Picture picture : artist.getPaintedPictures()) {
            picture.setAuthor(null);// Remove reference to the artist.
        }
    }
}
