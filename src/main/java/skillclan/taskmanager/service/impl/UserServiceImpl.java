package skillclan.taskmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.mapper.UserMapper;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    // Зберігаємо користувачів в HashMap для швидкості та зручності
    private final Map<Integer, User> USERS = new HashMap<>();

    // Для генерації userId використовуємо потокобезпечний Integer
    private final AtomicInteger USER_ID_GENERATOR = new AtomicInteger();

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        final int userId = USER_ID_GENERATOR.incrementAndGet();
        User user = userMapper.userDtoToUser(userDto);
        user.setId(userId);
        USERS.put(userId, user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> readAll() {
        return new ArrayList<>(USERS.values()).stream().map(user -> userMapper.userToUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public UserDto read(int id) {
        User user = USERS.get(id);
        if (user == null) {
            return null;
        }
        return userMapper.userToUserDto(user);
    }

    @Override
    public boolean update(UserDto userDto, int id) {
        if (USERS.containsKey(id)){
            User user = userMapper.userDtoToUser(userDto);
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
