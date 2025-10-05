package skillclan.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.mapper.UserMapper;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
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
        User createdUser = userService.create(user);
        return new ResponseEntity<>(userMapper.userToUserDto(createdUser), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        final List<User> users = userService.readAll();
        return new ResponseEntity<>(users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable (name = "id") int id){
        final User user = userService.read(id);
        return (user == null) //Next time, this will be replaced with error handling
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : new ResponseEntity<>(userMapper.userToUserDto(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@RequestBody UserDto userDto, @PathVariable (name = "id") int id){
        User user = userMapper.userDtoToUser(userDto);
        final User updatedUser = userService.update(user, id);
        return (updatedUser == null) //Next time, this will be replaced with error handling
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(userMapper.userToUserDto(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable (name = "id") int id){
        final boolean deleted = userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
