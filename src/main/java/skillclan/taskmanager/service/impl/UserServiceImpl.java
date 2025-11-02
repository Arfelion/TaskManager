package skillclan.taskmanager.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import skillclan.taskmanager.exception.EntityNotFoundException;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.UserRepository;
import skillclan.taskmanager.service.UserService;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public static final String CACHE_NAME = "users";

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        return userRepository.create(user).orElse(null);
    }
    
    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    @Override
    public User read(int id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " was not found"));
    }

    @CachePut(value = CACHE_NAME, key = "#user.id")
    @Override
    public User update(User user, int id) {
        boolean updated = userRepository.update(user, id);
        if (!updated){
            throw new EntityNotFoundException("User with id=" + id + " was not found");
        }
        user.setId(id);
        return user;
    }

    @CacheEvict(value = CACHE_NAME, key = "#id")
    @Override
    public boolean delete(int id) {
        return userRepository.delete(id);
    }
}
