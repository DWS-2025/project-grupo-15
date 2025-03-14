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

/*
        // Create sample users
        User alex = new User("Alex", "ponisalvaje@gmail.com");
        User samu = new User("Samu", "sosacaustica@hotmail.com");

        userRepository.save(alex);
        userRepository.save(samu);


 */

        // Create sample artists
        Artist unknown = new Artist("", "Artista Desconocido", "");
        Artist daVinci = new Artist("Leonardo", "DaPichi", "1400");
        Artist daVinchi = new Artist("Webonardo", "DePincho", "1500");

        artistRepository.save(daVinci);
        artistRepository.save(daVinchi);
        artistRepository.save(unknown);

      // Create a sample picture
        Picture monaLisa = new Picture("Mona Picha", "1900");
        Picture marioConda = new Picture("MarioConda", "2000");

        monaLisa.setArtist(daVinci);
        marioConda.setArtist(daVinchi);

        // Create sample comments
        Comment commentSample1 = new Comment("Amazing");
        Comment commentSample2 = new Comment("Disgusting");
        Comment commentSample3 = new Comment("Pretty");
        Comment commentSample4 = new Comment("Beautiful");

        monaLisa.addComment(commentSample1);
        monaLisa.addComment(commentSample2);
        marioConda.addComment(commentSample3);
        marioConda.addComment(commentSample4);

        pictureRepository.save(monaLisa);
        pictureRepository.save(marioConda);
    }
}
