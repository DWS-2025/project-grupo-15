package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Artist;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ArtistRepository {

    private AtomicLong nextId = new AtomicLong(0);
    private ConcurrentHashMap<Long, Artist> artistMap = new ConcurrentHashMap<>();

    public List<Artist> getArtists() {
        return new ArrayList<>(artistMap.values());
    }

    public Artist getArtist(long id) {
        return artistMap.get(id);
    }

    public void save(Artist artist) {
        long id = artist.getId();
        if(id == 0){
            id = nextId.incrementAndGet();
            artist.setId(id);
        }
        artistMap.put(id, artist);
    }

    public void delete(Artist artist) {
        artistMap.remove(artist.getId());
        }
    }
