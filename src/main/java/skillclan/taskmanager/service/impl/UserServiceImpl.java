package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {


    // Зберігаємо користувачів в HashMap для швидкості та зручності
    private final Map<Integer, User> USERS = new HashMap<>();

    // Для генерації userId використовуємо потокобезпечний Integer
    private final AtomicInteger USER_ID_GENERATOR = new AtomicInteger();

    public void resetUsers(){
        USERS.clear();
        USER_ID_GENERATOR.set(0);
    }

    @Override
    public User create(User user) {
        final int userId = USER_ID_GENERATOR.incrementAndGet();
        user.setId(userId);
        USERS.put(userId, user);
        return user;
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(USERS.values());
    }

    @Override
    public User read(int id) {
        return USERS.get(id);
    }

    @Override
    public boolean update(User user, int id) { // to UserDto
        if (USERS.containsKey(id)){
            user.setId(id);
            USERS.put(id, user);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return USERS.remove(id) != null;
    }
}
