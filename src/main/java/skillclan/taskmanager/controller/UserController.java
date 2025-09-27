package skillclan.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserMapper userMapper;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody UserDto userDto){
        User user = userMapper.userDtoToUser(userDto);
        return userMapper.userToUserDto(userService.create(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        final List<User> users = userService.readAll();
        return (users != null && !users.isEmpty())
                ? new ResponseEntity<>(users.stream()
                .map(user -> userMapper.userToUserDto(user))
                .collect(Collectors.toList()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable (name = "id") int id){
        final User user = userService.read(id);
        return (user != null)
            ? new ResponseEntity<>(userMapper.userToUserDto(user), HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable (name = "id") int id, @RequestBody UserDto userDto){
        User user = userMapper.userDtoToUser(userDto);
        final boolean updated = userService.update(user, id);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable (name = "id") int id){
        final boolean deleted = userService.delete(id);
        return  deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
