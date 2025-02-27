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

    @PostConstruct
    public void init(){
        User Alex = new User("Alex", "ponisalvaje@gmail.com");
        User Samu = new User("Samu", "sosacaustica@hotmail.com");

        Artist DaVinci = new Artist("Leonardo", "DaPichi", "1400");

        Picture MonaLisa = new Picture("Mona Picha", "La obra mas corta de la historia", "monapicha.jpeg", DaVinci);

        Comment commentSample1 = new Comment("Amazing");
        Comment commentSample2 = new Comment("Disgusting");

        commentSample1.setAuthor(Alex);
        commentSample2.setAuthor(Samu);

        MonaLisa.getComments().add(commentSample1);
        MonaLisa.getComments().add(commentSample2);

        commentRepository.save(commentSample1);
        commentRepository.save(commentSample2);

        pictureRepository.save(MonaLisa);
        userRepository.save(Samu);
        userRepository.save(Alex);
        artistRepository.save(DaVinci);
    }

}
