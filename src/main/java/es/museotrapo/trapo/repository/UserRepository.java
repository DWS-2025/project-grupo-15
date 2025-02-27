package es.museotrapo.trapo.repository;

import es.museotrapo.trapo.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserRepository {

    private AtomicLong nextId = new AtomicLong(0);
    private ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();

    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
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
