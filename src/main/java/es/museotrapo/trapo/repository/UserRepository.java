package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserRepository {

    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();

    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    public void save(User user) {
        long id = user.getId();
        if(id == 0){
            id = nextId.incrementAndGet();
            user.setId(id);
        }
        userMap.put(id, user);
    }
}
