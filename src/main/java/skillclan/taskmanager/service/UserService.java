package skillclan.taskmanager.service;

import skillclan.taskmanager.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    List<User> readAll();

    User read(int id);

    User update(User user, int id);

    boolean delete(int id);
}
