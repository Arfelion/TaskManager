package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.UserRepository;
import skillclan.taskmanager.service.UserService;

import java.util.List;
import java.util.Optional;


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
    public boolean update(User user, int id) {
        Optional<User> userOld = userRepository.findById(id);
        if (userOld.isPresent()){
            if (user.getName() == null || user.getName().isBlank()){
                user.setName(userOld.get().getName());
            }
            if (user.getEmail() == null || user.getEmail().isBlank()){
                user.setEmail(userOld.get().getEmail());
            }
            if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()){
                user.setPhoneNumber(userOld.get().getPhoneNumber());
            }
            return userRepository.update(user, id);
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return userRepository.delete(id);
    }
}
