package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> findAll() {
        return artistRepository.getArtists();
    }

    public boolean paintedPicture(Picture picture, long id) {
        return artistRepository.getArtist(id).getPaintedPictures().add(picture);
    }

    public void removePicture(long id, Picture picture) {
        Artist artist = artistRepository.getArtist(id);
        if(paintedPicture(picture, id)){
            List<Picture> updatedPictures = artist.getPaintedPictures();
            updatedPictures.remove(picture);
            artist.setPaintedPictures(updatedPictures);
        }
        artistRepository.save(artist);
    }

    public void deleteArtist(Artist artist) {
        artistRepository.delete(artist);
    }
}
