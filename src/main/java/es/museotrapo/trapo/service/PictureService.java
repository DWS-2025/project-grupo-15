package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private ImageService imageService;

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
    public void save(Picture picture, Long artistId, MultipartFile imageFile) throws IOException {
        // Validate if all required fields are filled
        if (picture.getDate() == null || picture.getName() == null || artistId == null) {
            throw new IllegalArgumentException("NO pueden haber campos vacios"); // Throw error if any field is empty
        }

        // Create and save the image, then associate it with the picture
        picture.setImageFilename(imageService.createImage(imageFile)); // Save the image and set the filename

        picture.setArtist(artistService.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Artist not found")));
        pictureRepository.save(picture);
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

        picture.getComments().clear();
        pictureRepository.deleteById(picture.getId());
    }

}