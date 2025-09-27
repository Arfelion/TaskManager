package skillclan.taskmanager.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class TaskController {
    @GetMapping
    public ResponseEntity hello() {
        return ResponseEntity.ok("Тут може бути ваша реклама - Hello");
    }
    @GetMapping("/world")
    public ResponseEntity world() {
        return ResponseEntity.ok("Hello World");
    }
}
