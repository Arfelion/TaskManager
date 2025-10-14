package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.UserRepository;
import skillclan.taskmanager.service.UserService;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

    @Override
    public User read(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User update(User user, int id) {
        boolean updated = userRepository.update(user, id);
        user.setId(id);
        return updated ? user : null;
    }

    @Override
    public boolean delete(int id) {
        return userRepository.delete(id);
    }
}
