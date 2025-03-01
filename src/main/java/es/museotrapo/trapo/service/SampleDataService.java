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

        Artist unknown = new Artist("", "Artista Desconocido", "");
        Artist daVinci = new Artist("Leonardo", "DaPichi", "1400");
        Artist daVinchi = new Artist("Webonardo", "DePincho", "1500");

        Picture MonaLisa = new Picture("Mona Picha", "1900");
        MonaLisa.setImageFilename("monapicha.jpeg");
        MonaLisa.setAuthor(unknown);
        MonaLisa.getUserLikes().add(Alex);
        Alex.getLikedPictures().add(MonaLisa);
        unknown.getPaintedPictures().add(MonaLisa);

        Comment commentSample1 = new Comment("Amazing");
        commentSample1.setAuthor(Alex);
        MonaLisa.getComments().add(commentSample1);
        Alex.getComments().add(commentSample1);
        commentRepository.save(commentSample1);
    
        Comment commentSample2 = new Comment("Disgusting");
        commentSample2.setAuthor(Samu);
        MonaLisa.getComments().add(commentSample2);
        Samu.getComments().add(commentSample2);
        commentRepository.save(commentSample2);

        pictureRepository.save(MonaLisa);
        userRepository.save(Samu);
        userRepository.save(Alex);
        artistRepository.save(unknown);
        artistRepository.save(daVinci);
        artistRepository.save(daVinchi);
        
    }

}
