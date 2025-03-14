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
        User alex = new User("Alex", "ponisalvaje@gmail.com");
        User samu = new User("Samu", "sosacaustica@hotmail.com");


        /*
        // Create sample artists
        Artist unknown = new Artist("", "Artista Desconocido", "");
        Artist daVinci = new Artist("Leonardo", "DaPichi", "1400");
        Artist daVinchi = new Artist("Webonardo", "DePincho", "1500");

        // Create a sample picture
        Picture monaLisa = new Picture("Mona Picha", "1900");
        monaLisa.setImageFilename("monapicha.jpeg");
        monaLisa.setartist(unknown); // Set the artist for the picture
        monaLisa.getUserLikes().add(alex); // Alex likes the picture
        alex.getLikedPictures().add(monaLisa); // Add the picture to Alex's liked pictures
        unknown.getPaintedPictures().add(monaLisa); // Add the picture to the artist's paintings

        Picture marioConda = new Picture("MarioConda", "2000");
        marioConda.setImageFilename("MarioConda.jpg");
        marioConda.setartist(daVinci); // Set the artist for the picture
        marioConda.getUserLikes().add(samu); // Alex likes the picture
        samu.getLikedPictures().add(marioConda); // Add the picture to Alex's liked pictures
        daVinci.getPaintedPictures().add(marioConda); // Add the picture to the artist's paintings

        // Create sample comments
        Comment commentSample1 = new Comment("Amazing");
        commentSample1.setAuthor(alex); // Set the author of the comment
        monaLisa.getComments().add(commentSample1); // Add the comment to the picture
        alex.getComments().add(commentSample1); // Add the comment to Alex's list of comments
        commentRepository.save(commentSample1); // Save the comment to the repository

        Comment commentSample2 = new Comment("Disgusting");
        commentSample2.setAuthor(samu); // Set the author of the second comment
        monaLisa.getComments().add(commentSample2); // Add the second comment to the picture
        samu.getComments().add(commentSample2); // Add the second comment to Samu's list of comments
        commentRepository.save(commentSample2); // Save the second comment to the repository

        Comment commentSample3 = new Comment("Pretty");
        commentSample3.setAuthor(alex);
        marioConda.getComments().add(commentSample3);
        alex.getComments().add(commentSample3);
        commentRepository.save(commentSample3);

        Comment commentSample4 = new Comment("Beautiful");
        commentSample4.setAuthor(samu);
        marioConda.getComments().add(commentSample4);
        samu.getComments().add(commentSample4);
        commentRepository.save(commentSample4);

        // Save all entities to the respective repositories
        pictureRepository.save(monaLisa);
        pictureRepository.save(marioConda);
        userRepository.save(samu);
        userRepository.save(alex);
        artistRepository.save(unknown);
        artistRepository.save(daVinci);
        artistRepository.save(daVinchi);
        */
    }
}
