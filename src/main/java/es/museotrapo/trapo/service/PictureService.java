package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArtistService artistService;

    public List<Picture> getPictures() {
        return pictureRepository.getPictures();
    }

    public Optional<Picture> getPictureById(int id) {
        return pictureRepository.findById(id);
    }

    public void save (Picture picture) {
        pictureRepository.save(picture);
    }

    public void update(Picture oldPicture, Picture picture) {
        oldPicture.setId(picture.getId());
        oldPicture.setDescription(picture.getDescription());
        oldPicture.setAuthor(oldPicture.getAuthor());
        oldPicture.setImageFilename(oldPicture.getImageFilename());
        pictureRepository.save(oldPicture);
    }

    public void delete(Picture picture) {
        for(User user: picture.getUserLikes()){
            user.getLikedPosts().remove(picture);
        }
        picture.getUserLikes().clear();

        for(Comment comment: picture.getComments()){
            commentService.delete(comment.getId(), picture);
        }

        picture.getComments().clear();
        pictureRepository.deleteById(picture.getId());
    }
}
