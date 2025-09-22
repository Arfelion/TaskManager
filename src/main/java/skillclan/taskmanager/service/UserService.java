package skillclan.taskmanager.service;

import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.model.User;

import java.util.List;

public interface UserService {

    // Створення нового користувача (POST with BODY)
    UserDto create(UserDto userDto);

    // Отримання списку користувачів (GET)
    List<UserDto> readAll();

    // Отримання користувача по ID (GET with param)
    UserDto read(int id);

    // Оновлення даних інсуючого користувача по ID (PUT with BODY)
    boolean update(UserDto userDto, int id);

    // Видалення існуючого користувача по ID (DELETE with param)
    boolean delete(int id);
}
