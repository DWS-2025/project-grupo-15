package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that manages picture-related operations.
 */
@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArtistService artistService;

    /**
     * Retrieves all pictures stored in the repository.
     *
     * @return List of all pictures.
     */
    public List<Picture> findAll() {
        return pictureRepository.findAll();
    }

    /**
     * Finds a picture by its unique ID.
     *
     * @param id The ID of the picture.
     * @return An Optional containing the picture if found.
     */
    public Optional<Picture> findById(long id) {
        return pictureRepository.findById(id);
    }

    /**
     * Saves a new picture and associates it with an artist.
     *
     * @param picture  The picture to be saved.
     * @param artistId The ID of the artist who created the picture.
     */
    public void save(Picture picture, Long artistId) {
        picture.setAuthor(
                artistService.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Artist not found")));
        pictureRepository.save(picture);
    }

    /**
     * Updates an existing picture's metadata.
     *
     * @param oldPicture The existing picture to be updated.
     * @param picture    The updated picture details.
     */
    public void update(Picture oldPicture, Picture picture) {
        oldPicture.setId(picture.getId());
        oldPicture.setDate(picture.getDate());
        oldPicture.setAuthor(oldPicture.getAuthor()); // No change in author
        oldPicture.setImageFilename(oldPicture.getImageFilename()); // No change in image filename
        pictureRepository.save(oldPicture);
    }

    /**
     * Deletes a picture and removes all its associations.
     *
     * @param picture The picture to be deleted.
     */
    public void delete(Picture picture) {
        // Remove the picture from all users' liked lists
        for (User user : picture.getUserLikes()) {
            user.getLikedPictures().remove(picture);
        }
        picture.getUserLikes().clear();

        // Remove all comments associated with the picture
        List<Comment> list = picture.getComments();
        for (int i = list.size() - 1; i >= 0; --i) {
            commentService.delete(list.get(i).getId(), picture);
        }

        artistService.deletePicture(picture.getId(), picture);// Remove the picture from the artist's painted pictures
                                                              // list

        picture.getComments().clear();
        pictureRepository.deleteById(picture.getId());
    }

    /**
     * Removes the reference to an artist from all pictures they were associated
     * with.
     *
     * @param artist The artist whose reference should be removed from pictures.
     */
    public void deleteArtistInPicture(Artist artist) {
        pictureRepository.deleteArtistInPicture(artist);
    }
}