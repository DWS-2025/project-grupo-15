package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that manages the in-memory list of pictures and artists in the museum.
 * This class acts as a temporary storage, which may later be replaced by a database.
 */
@Service
public class MusseumService {

    // List to store pictures in memory
    private List<Picture> pictures = new ArrayList<>();

    // List to store artists in memory
    private List<Artist> artists = new ArrayList<>();

    /**
     * Retrieves the list of pictures in the museum.
     *
     * @return List of pictures.
     */
    public List<Picture> getPictures() {
        return pictures;
    }

    /**
     * Updates the list of pictures in the museum.
     *
     * @param pictures The new list of pictures.
     */
    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     * Retrieves the list of artists in the museum.
     *
     * @return List of artists.
     */
    public List<Artist> getArtists() {
        return artists;
    }

    /**
     * Updates the list of artists in the museum.
     *
     * @param artists The new list of artists.
     */
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}