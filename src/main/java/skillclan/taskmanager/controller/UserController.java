package skillclan.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.mapper.UserMapper;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        User user = userMapper.userDtoToUser(userDto);
        User user1 = userService.create(user);
        return new ResponseEntity<>(userMapper.userToUserDto(user1), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        final List<User> users = userService.readAll();
        return (users != null && !users.isEmpty())
                ? new ResponseEntity<>(users.stream()
                .map(user -> userMapper.userToUserDto(user))
                .collect(Collectors.toList()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable (name = "id") int id){
        final User user = userService.read(id);
        return (user != null)
            ? new ResponseEntity<>(userMapper.userToUserDto(user), HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@RequestBody UserDto userDto, @PathVariable (name = "id") int id){
        User user = userMapper.userDtoToUser(userDto);
        final User updated = userService.update(user, id);
        return (updated != null)
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable (name = "id") int id){
        final boolean deleted = userService.delete(id);
        return  deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
