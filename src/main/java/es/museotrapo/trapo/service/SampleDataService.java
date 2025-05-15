package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Comment;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.model.User;
import es.museotrapo.trapo.repository.ArtistRepository;
import es.museotrapo.trapo.repository.PictureRepository;
import es.museotrapo.trapo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service that initializes sample data in the application.
 * This class is responsible for populating the database with initial data.
 */
@Service
public class SampleDataService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method executed after the beans are created and the dependencies are
     * injected.
     * It populates the system with initial data.
     */
    @PostConstruct
    public void init() {

        // Create sample users
        User user1 = userRepository.save(new User("user1", "ponisalvaje@yahoo.es", passwordEncoder.encode("pass1"), "USER"));
        User user2 = userRepository.save(new User("user2", "luisafer@yahoo.es", passwordEncoder.encode("pass2"), "USER"));
        User user3 = userRepository.save(new User("user3", "arturito@yahoo.es", passwordEncoder.encode("pass3"), "USER"));
        User user4 = userRepository.save(new User("user4", "nose@yahoo.es", passwordEncoder.encode("pass4"), "USER"));
        User user5 = userRepository.save(new User("user5", "queponer@yahoo.es", passwordEncoder.encode("pass5"), "USER"));
        User admin = userRepository.save(new User("admin", "sosacaustica@hotmail.com", passwordEncoder.encode("adminpass"), "ADMIN", "USER"));

        // Create sample artists
        Artist unknown = new Artist("", "Artista Desconocido", "");
        Artist daVinci = new Artist("Leonardo", "DaPichi", "1400");
        Artist daVinchi = new Artist("Webonardo", "DePincho", "1500");
        Artist daVinicius = new Artist("Tumai", "daVinicius", "1500");
        Artist daPincho = new Artist("dolomites", "daPinchote", "1500");
        Artist deLoite = new Artist("Kilian", "deLoite", "1500");
        Artist NoseYamas = new Artist("Weardo", "NoseYamas", "1500");
        Artist LaRuina = new Artist("onardo", "LaRuina", "1500");
        Artist EsteNoSbe = new Artist("Webo", "EsteNoSbe", "1500");
        Artist PatronDelMal = new Artist("pariguayo", "PatronDelMal", "1500");
        Artist SIoQUEE = new Artist("asindeuq", "SIoQUEE", "1500");
        Artist TUTECallas = new Artist("asi", "TUTECallas", "1500");

        artistRepository.save(daVinci);
        artistRepository.save(daVinchi);
        artistRepository.save(unknown);
        artistRepository.save(daVinicius);
        artistRepository.save(daPincho);
        artistRepository.save(deLoite);
        artistRepository.save(NoseYamas);
        artistRepository.save(LaRuina);
        artistRepository.save(EsteNoSbe);
        artistRepository.save(PatronDelMal);
        artistRepository.save(SIoQUEE);
        artistRepository.save(TUTECallas);

        // Create a sample picture
        Picture monaLisa = new Picture("Mona Picha", "1900");
        Picture marioConda = new Picture("MarioConda", "2000");

        monaLisa.setImageFile(imageService.localImageToBlob("pictures/monapicha.jpeg"));
        marioConda.setImageFile(imageService.localImageToBlob("pictures/MarioConda.jpg"));

        monaLisa.setImage("true");
        marioConda.setImage("true");

        monaLisa.setArtist(daVinci);
        marioConda.setArtist(daVinchi);

        monaLisa.getUserLikes().add(user1);
        marioConda.getUserLikes().add(user2);

        // Create sample comments
        Comment commentSample1 = new Comment("Amazing");
        Comment commentSample2 = new Comment("Disgusting");
        Comment commentSample3 = new Comment("Pretty");
        Comment commentSample4 = new Comment("Beautiful");

        commentSample1.setPicture(monaLisa);
        commentSample2.setPicture(monaLisa);
        commentSample3.setPicture(marioConda);
        commentSample4.setPicture(marioConda);

        commentSample1.setAuthor(user1);
        commentSample2.setAuthor(user2);
        commentSample3.setAuthor(user2);
        commentSample4.setAuthor(user1);

        monaLisa.getComments().add(commentSample1);
        monaLisa.getComments().add(commentSample2);
        marioConda.getComments().add(commentSample3);
        marioConda.getComments().add(commentSample4);

        pictureRepository.save(monaLisa);
        pictureRepository.save(marioConda);
    }
}
