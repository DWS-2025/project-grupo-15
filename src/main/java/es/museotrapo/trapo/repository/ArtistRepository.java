package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import es.museotrapo.trapo.model.Picture;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Component
public class ArtistRepository {

    private AtomicLong nextId = new AtomicLong(1L);

    private ConcurrentHashMap<Long, Artist> artistMap = new ConcurrentHashMap<>();

    public List<Artist> findAll() {
        return artistMap.values().stream().toList();
    }

    public Optional<Artist> findById(long id) {
        return Optional.ofNullable(artistMap.get(id));
    }

    public void save(Artist artist) {
        long id = artist.getId();
        if(id == 0){
           id = nextId.incrementAndGet();
           artist.setId(id);
        }

        artistMap.put(id, artist);
    }

    public void deleteById(long id) {
        artistMap.remove(id);
    }

    public void addPicture(Picture picture, Long id) {
        List<Picture> pictures;
        pictures = artistMap.get(id).getPaintedPictures();
        pictures.add(picture);
        artistMap.get(id).setPaintedPictures(pictures);
    }

    public void deletePicture(Picture picture, Long id){
        picture.getAuthor().getPaintedPictures().remove(picture);
    }
}

