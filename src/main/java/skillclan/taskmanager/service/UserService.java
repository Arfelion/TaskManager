package skillclan.taskmanager.service;

import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.model.User;

import java.util.List;

public interface UserService {

    // Створення нового користувача (POST with BODY)
    User create(User user);

    // Отримання списку користувачів (GET)
    List<User> readAll();

    // Отримання користувача по ID (GET with param)
    User read(int id);

    // Оновлення даних інсуючого користувача по ID (PUT with BODY)
    boolean update(User user, int id);

    // Видалення існуючого користувача по ID (DELETE with param)
    boolean delete(int id);
}
