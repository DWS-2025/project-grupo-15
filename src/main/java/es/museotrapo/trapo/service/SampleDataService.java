package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.Username;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.repository.CommentRepository;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UsernameRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
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
    private UsernameRepository usernameRepository;

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
        Username alex = usernameRepository.save(new Username("Alex", "ponisalvaje@gmail.com"));
        Username samu = usernameRepository.save(new Username("Samu", "sosacaustica@hotmail.com"));

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

        /*
        monaLisa.setImageFilename("monapicha.jpeg");
        marioConda.setImageFilename("MarioConda.jpg");
         */

        monaLisa.setArtist(daVinci);
        marioConda.setArtist(daVinchi);

        monaLisa.getUserLikes().add(alex);
        marioConda.getUserLikes().add(samu);

        // Create sample comments
        Comment commentSample1 = new Comment("Amazing");
        Comment commentSample2 = new Comment("Disgusting");
        Comment commentSample3 = new Comment("Pretty");
        Comment commentSample4 = new Comment("Beautiful");

        commentSample1.setAuthor(samu);
        commentSample2.setAuthor(alex);
        commentSample3.setAuthor(alex);
        commentSample4.setAuthor(samu);

        monaLisa.getComments().add(commentSample1);
        monaLisa.getComments().add(commentSample2);
        marioConda.getComments().add(commentSample3);
        marioConda.getComments().add(commentSample4);

        pictureRepository.save(monaLisa);
        pictureRepository.save(marioConda);
    }
}
