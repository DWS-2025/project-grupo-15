package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.repository.PictureRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Artist entities.
 * Provides methods to interact with the ArtistRepository and PictureRepository.
 */
@Service
public class ArtistService {

    // Injecting ArtistRepository to manage artist data.
    @Autowired
    private ArtistRepository artistRepository;

    // Injecting PictureRepository to manage pictures related to artists.
    @Autowired
    private PictureRepository pictureRepository;

    /**
     * Retrieves all artists stored in the repository.
     *
     * @return A list of all artists.
     */
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    /**
     * Finds an artist by their ID.
     *
     * @param id The ID of the artist to retrieve.
     * @return An Optional containing the artist if found, or empty if not found.
     */
    public Optional<Artist> findById(long id) {
        return artistRepository.findById(id);
    }

    /**
     * Saves a new artist or updates an existing one.
     *
     * @param artist The artist to save.
     */
    public void save(Artist artist) {
        if (artist.getBirthDate() == null || artist.getName() == null || artist.getNickname() == null) {
            throw new IllegalArgumentException("NO pueden haber campos vacios");
        }
        artistRepository.save(artist);
    }

    /**
     * Updates an existing artist's details.
     *
     * @param oldArtist     The existing artist to update.
     * @param updatedArtist The artist containing updated details.
     */
    public void update(Artist oldArtist, Artist updatedArtist) {
        oldArtist.setName(updatedArtist.getName());
        oldArtist.setNickname(updatedArtist.getNickname());
        oldArtist.setBirthDate(updatedArtist.getBirthDate());
        artistRepository.save(oldArtist);
    }

    /**
     * Deletes an artist from the repository.
     * Before deleting, it removes the association between the artist and their
     * pictures.
     *
     * @param artist The artist to delete.
     */
    public void delete(Artist artist) {
        artistRepository.deleteById(artist.getId());
    }

    /**
     * Adds a picture to an artist's collection.
     *
     * @param id      The ID of the artist.
     * @param picture The picture to add.
     */
    public void addPicture(Long id, Picture picture) {
        artistRepository.addPicture(picture, id);
    }

    /**
     * Removes a picture from an artist's collection.
     *
     * @param id      The ID of the artist.
     * @param picture The picture to remove.
     */
    public void deletePicture(Long id, Picture picture) {
        artistRepository.deletePicture(picture, id);
    }
}