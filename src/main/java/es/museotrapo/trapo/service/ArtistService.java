package es.museotrapo.trapo.service;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import es.museotrapo.trapo.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> getArtists() {
        return artistRepository.getArtists();
    }

    public Artist getArtistById(long id) {
        return artistRepository.getArtist(id);
    }

    public boolean paintedPicture(Picture picture, long id) {
        return artistRepository.getArtist(id).getPaintedPictures().add(picture);
    }

    public void save (Artist artist) {
        artistRepository.save(artist);
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
