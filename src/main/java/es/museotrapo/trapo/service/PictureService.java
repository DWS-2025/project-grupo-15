package es.museotrapo.trapo.service;

import es.museotrapo.trapo.dto.CommentDTO;
import es.museotrapo.trapo.dto.PictureDTO;
import es.museotrapo.trapo.dto.PictureMapper;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


/**
 * Service class that manages picture-related operations.
 * Handles operations like retrieving, creating, deleting, and modifying pictures.
 */
@Service // Spring annotation indicating this is a service class
public class PictureService {

    // Injecting repositories and services needed for picture operations
    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private PictureMapper mapper;

    /**
     * Retrieves all pictures and converts them to PictureDTOs.
     *
     * @return a collection of PictureDTOs.
     */
    public Collection<PictureDTO> getPictures() {
        return toDTOs(pictureRepository.findAll());
    }

    /**
     * Retrieves a paginated list of pictures and converts them to PictureDTOs.
     *
     * @param pageable the pagination information.
     * @return a Page of PictureDTOs.
     */
    public Page<PictureDTO> getPictures(Pageable pageable) {
        Page<Picture> picturePage = pictureRepository.findAll(pageable);
        return convertToDTOPage(picturePage);
    }

    /**
     * Retrieves a specific picture by ID and converts it to a PictureDTO.
     *
     * @param id the ID of the picture.
     * @return the PictureDTO of the picture with the given ID.
     * @throws NoSuchElementException if the picture is not found.
     */
    public PictureDTO getPicture(long id) {
        return toDTO(pictureRepository.findById(id).orElseThrow());
    }

    /**
     * Creates a new picture and associates it with an artist and an image file.
     *
     * @param pictureDTO the data transfer object containing picture details.
     * @param artistId   the ID of the artist to associate with the picture.
     * @param imageFile  the image file to associate with the picture.
     * @throws IOException              if there is an error handling the image file.
     * @throws IllegalArgumentException if any required fields are empty.
     */
    public void createPicture(PictureDTO pictureDTO, Long artistId, MultipartFile imageFile) throws IOException {
        // Validate if all required fields are filled
        Picture picture = toDomain(pictureDTO);
        if (picture.getDate().isEmpty() || picture.getName().isEmpty() || artistId == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("NO pueden haber campos vacios"); // Error if any field is empty
        }
        // If the image file is provided, mark it as "True" and set the image file
        if (!imageFile.isEmpty()) {
            picture.setImage("True");
        }

        // Generate a Blob from the image file and associate it with the picture
        picture.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        picture.setArtist(artistService.toDomain(artistService.getArtist(artistId))); // Associate the artist with the picture
        pictureRepository.save(picture); // Save the picture
    }

    /**
     * Creates a new picture via a REST endpoint, associating it with an artist.
     *
     * @param pictureDTO the data transfer object containing picture details.
     * @return the PictureDTO of the newly created picture.
     * @throws IOException if there is an error handling the picture data.
     */
    public PictureDTO createPictureREST(PictureDTO pictureDTO) throws IOException {
        // Convert the PictureDTO to a Picture domain object and associate the artist
        Picture picture = toDomain(pictureDTO);
        picture.setArtist(artistService.toDomain(artistService.getArtist(pictureDTO.artistId())));
        pictureRepository.save(picture); // Save the picture
        return toDTO(picture); // Return the newly created picture as a DTO
    }

    /**
     * Creates and associates an image file to an existing picture via a REST endpoint.
     *
     * @param picId       the ID of the picture to update.
     * @param location    the URI location of the image.
     * @param inputStream the input stream of the image file.
     * @param size        the size of the image file.
     * @throws IOException if there is an error handling the image file.
     */
    public void createPictureImageREST(Long picId, URI location, InputStream inputStream, long size) throws IOException {
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        picture.setImage(location.toString()); // Set the image location
        picture.setImageFile(BlobProxy.generateProxy(inputStream, size)); // Set the image file
        pictureRepository.save(picture); // Save the picture with the new image
    }

    /**
     * Deletes a picture and its associated data (like user likes and comments).
     *
     * @param pictureDTO the data transfer object representing the picture to delete.
     * @return the PictureDTO of the deleted picture.
     */
    @Transactional
    public PictureDTO deletePicture(PictureDTO pictureDTO) {
        Picture picture = pictureRepository.findById(pictureDTO.id()).orElseThrow();

        // Remove the picture from all users' liked lists
        for (User user : picture.getUserLikes()) {
            user.getLikedPictures().remove(picture);
        }
        picture.getUserLikes().clear(); // Clear the list of users who liked the picture
        pictureRepository.save(picture);

        // Delete all comments associated with the picture
        List<Comment> comments = picture.getComments();
        for (int i = comments.size() - 1; i >= 0; --i) {
            commentService.deleteCommentHelp(comments.get(i).getId(), pictureDTO.id());
        }

        picture.getComments().clear(); // Clear the list of comments
        pictureRepository.save(picture);
        pictureRepository.deleteByIdCustom(picture.getId()); // Delete the picture
        return pictureDTO; // Return the deleted picture as a DTO
    }

    /**
     * Retrieves the image of a picture.
     *
     * @param id the ID of the picture.
     * @return a Resource containing the image file.
     * @throws SQLException if the image file is not found or there is an error accessing it.
     */
    public Resource getPictureImage(long id) throws SQLException {
        Picture picture = pictureRepository.findById(id).orElseThrow();
        if (picture.getImageFile() != null) {
            return new InputStreamResource(picture.getImageFile().getBinaryStream()); // Return the image as a resource
        } else {
            throw new NoSuchElementException(); // Throw an error if the image is not found
        }
    }

    /**
     * Adds a comment to a picture.
     *
     * @param commentDTO the comment to add.
     * @param picId      the ID of the picture to add the comment to.
     * @return the updated PictureDTO with the new comment.
     */
    public CommentDTO addComment(CommentDTO commentDTO, Long picId) {
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Comment comment = commentService.toDomain(commentDTO);
        String SanitizedMessage = SanitizeService.sanitize(comment.getMessage());
        comment.setMessage(SanitizedMessage);
        comment.setAuthor(userService.toDomain(userService.getLoggedUserDTO())); // Set the logged-in user as the author of the comment
        picture.getComments().add(comment); // Add the comment to the picture's list of comments
        comment.setPicture(picture);
        commentService.addComment(comment);
        pictureRepository.save(picture); // Save the picture with the new comment
        return commentService.toDTO(comment); // Return the updated picture as a DTO
    }

    /**
     * Removes a comment from a picture.
     *
     * @param commentId the ID of the comment to remove.
     * @param picId     the ID of the picture to remove the comment from.
     * @return the updated PictureDTO after removing the comment.
     */
    public CommentDTO removeComment(Long commentId, long picId, Authentication authentication) {
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Comment comment = commentService.toDomain(commentService.getComment(commentId));
        commentService.deleteComment(commentId, picId, authentication); // Delete the comment from the repository
        return commentService.toDTO(comment); // Return the updated picture as a DTO
    }

    public Collection<CommentDTO> getComments(long picId) {
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Collection<Comment> comments = picture.getComments();
        return commentService.toDTOs(comments);
    }

    /**
     * Retrieves the total count of pictures in the repository.
     *
     * @return the number of pictures.
     */
    public long count() {
        return pictureRepository.count();
    }

    /**
     * Converts a Page of Picture entities to a Page of PictureDTOs.
     *
     * @param picturePage the Page of Picture entities.
     * @return a Page of PictureDTOs.
     */
    public Page<PictureDTO> convertToDTOPage(Page<Picture> picturePage) {
        return new PageImpl<>(
                picturePage.getContent().stream().map(mapper::toDTO).collect(Collectors.toList()),
                picturePage.getPageable(),
                picturePage.getTotalElements()
        );
    }

    // Helper methods to convert between Picture entities and PictureDTOs

    private PictureDTO toDTO(Picture picture) {
        return mapper.toDTO(picture);
    }

    protected Picture toDomain(PictureDTO pictureDTO) {
        return mapper.toDomain(pictureDTO);
    }

    private Collection<PictureDTO> toDTOs(Collection<Picture> pictures) {
        return mapper.toDTOs(pictures);
    }
}
