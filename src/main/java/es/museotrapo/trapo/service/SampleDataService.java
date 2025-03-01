package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.repository.CommentRepository;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service that initializes sample data in the application.
 * This class is responsible for populating the database with initial data.
 */
@Service
public class SampleDataService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    /**
     * Method executed after the beans are created and the dependencies are
     * injected.
     * It populates the system with initial data.
     */
    @PostConstruct
    public void init() {
        // Create sample users
        User Alex = new User("Alex", "ponisalvaje@gmail.com");
        User Samu = new User("Samu", "sosacaustica@hotmail.com");

        // Create sample artists
        Artist unknown = new Artist("", "Artista Desconocido", "");
        Artist daVinci = new Artist("Leonardo", "DaPichi", "1400");
        Artist daVinchi = new Artist("Webonardo", "DePincho", "1500");

        // Create a sample picture
        Picture MonaLisa = new Picture("Mona Picha", "1900");
        MonaLisa.setImageFilename("monapicha.jpeg");
        MonaLisa.setAuthor(unknown); // Set the artist for the picture
        MonaLisa.getUserLikes().add(Alex); // Alex likes the picture
        Alex.getLikedPictures().add(MonaLisa); // Add the picture to Alex's liked pictures
        unknown.getPaintedPictures().add(MonaLisa); // Add the picture to the artist's paintings

        // Create sample comments
        Comment commentSample1 = new Comment("Amazing");
        commentSample1.setAuthor(Alex); // Set the author of the comment
        MonaLisa.getComments().add(commentSample1); // Add the comment to the picture
        Alex.getComments().add(commentSample1); // Add the comment to Alex's list of comments
        commentRepository.save(commentSample1); // Save the comment to the repository

        Comment commentSample2 = new Comment("Disgusting");
        commentSample2.setAuthor(Samu); // Set the author of the second comment
        MonaLisa.getComments().add(commentSample2); // Add the second comment to the picture
        Samu.getComments().add(commentSample2); // Add the second comment to Samu's list of comments
        commentRepository.save(commentSample2); // Save the second comment to the repository

        // Save all entities to the respective repositories
        pictureRepository.save(MonaLisa);
        userRepository.save(Samu);
        userRepository.save(Alex);
        artistRepository.save(unknown);
        artistRepository.save(daVinci);
        artistRepository.save(daVinchi);
    }
}
