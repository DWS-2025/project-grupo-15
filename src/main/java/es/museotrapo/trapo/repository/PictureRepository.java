package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.Picture;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PictureRepository {

    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Picture> pictureMap = new ConcurrentHashMap<>();

    public List<Picture> findAll() {
        return pictureMap.values().stream().toList();
    }

    public Optional<Picture> findById(long id) {
        return Optional.ofNullable(pictureMap.get(id));
    }

    public void save(Picture picture) {
        long id = picture.getId();
        if(id == 0){
            id = nextId.incrementAndGet();
            picture.setId(id);
        }
        pictureMap.put(id, picture);
   }

   public void deleteById(long id) {
        pictureMap.remove(id);
   }
}
