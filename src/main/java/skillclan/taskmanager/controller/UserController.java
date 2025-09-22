package skillclan.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody UserDto userDto){
        return userService.create(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        final List<UserDto> users = userService.readAll();
        return (users != null && !users.isEmpty())
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable (name = "id") int id){
        final UserDto userDto = userService.read(id);
        return (userDto != null)
            ? new ResponseEntity<>(userDto, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable (name = "id") int id, @RequestBody UserDto userDto){
        final boolean updated = userService.update(userDto, id);
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
