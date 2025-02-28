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

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findById(long id) {
        return artistRepository.findById(id);
    }

    public void save (Artist artist) {
        artistRepository.save(artist);
    }

    public void update(Artist oldArtist, Artist updatedArtist) {
		oldArtist.setName(updatedArtist.getName());
		oldArtist.setNickname(updatedArtist.getNickname());
        oldArtist.setBirthDate(updatedArtist.getBirthDate());
		artistRepository.save(oldArtist);
	}

    public void delete(Artist artist) {
        artistRepository.deleteById(artist.getId());
    }

    public void addPicture(Long id, Picture picture) {
        artistRepository.addPicture(picture, id);
    }
}
