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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

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
    private UserService userService;

    @Autowired
    private PictureMapper mapper;

    public Collection<PictureDTO> getPictures() {
        return toDTOs(pictureRepository.findAll());
    }

    public PictureDTO getPicture(long id) {
        return toDTO(pictureRepository.findById(id).orElseThrow());
    }

public PictureDTO createPicture(PictureDTO pictureDTO, Long artistId, MultipartFile imageFile) throws IOException {
        // Validate if all required fields are filled
        Picture picture = toDomain(pictureDTO);
        if (picture.getDate().isEmpty()|| picture.getName().isEmpty()|| artistId == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("NO pueden haber campos vacios"); // Throw error if any field is empty
        }
        if(!imageFile.isEmpty()) {
            picture.setImage("True");
        }
        // Create and save the image, then associate it with the picture
        picture.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        picture.setArtist(artistService.toDomain(artistService.getArtist(artistId)));
        pictureRepository.save(picture);
        return toDTO(picture);
    }

    public PictureDTO deletePicture(PictureDTO pictureDTO) {
        Picture picture = toDomain(pictureDTO);
        // Remove the picture from all users' liked lists
        for (User user : picture.getUserLikes()) {
            user.getLikedPictures().remove(picture);
        }
        picture.getUserLikes().clear();

        // Remove all comments associated with the picture
        List<Comment> comments = picture.getComments();
        for (int i = comments.size() - 1; i >= 0; --i) {
            commentService.deleteComment(comments.get(i).getId(), pictureDTO.id());
        }

        picture.getComments().clear();
        pictureRepository.deleteById(picture.getId());
        return pictureDTO;
    }

    public PictureDTO addComment(CommentDTO commentDTO, long picId) {
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Comment comment = commentService.toDomain(commentDTO);
        comment.setAuthor(userService.toDomain(userService.getLoggedUserDTO()));
        picture.getComments().add(comment);
        commentService.toDTO(comment);
        pictureRepository.save(picture);
        return toDTO(picture);
    }

    public PictureDTO removeComment(Long commentId, long picId) {
        Picture picture = pictureRepository.findById(picId).orElseThrow();
        Comment comment = commentService.toDomain(commentService.getComment(commentId));
        picture.getComments().remove(comment);
        pictureRepository.save(picture);
        commentService.deleteComment(commentId, picId);
        return toDTO(picture);
    }

    public Resource getPictureImage(long id) throws SQLException {

        Picture picture = pictureRepository.findById(id).orElseThrow();

        if(picture.getImageFile() != null) {
            return new InputStreamResource(picture.getImageFile().getBinaryStream());
        }else {
            throw new NoSuchElementException();
        }
    }
    private PictureDTO toDTO(Picture picture) {
        return mapper.toDTO(picture);
    }

    protected Picture toDomain(PictureDTO pictureDTO){
        return mapper.toDomain(pictureDTO);
    }

    private Collection<PictureDTO> toDTOs(Collection<Picture> pictures){
        return mapper.toDTOs(pictures);
    }
}