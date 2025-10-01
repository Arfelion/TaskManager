package skillclan.taskmanager.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.service.impl.TaskServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        return new ResponseEntity<>(taskService.create(task), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        final List<Task> tasks = taskService.readAll();
        return (tasks == null || tasks.isEmpty())
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable (name = "id") int id){
        Task task = taskService.read(id);
        return (task == null)
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Task> updateTaskById(@RequestBody Task task, @PathVariable (name = "id") int id){
            Task updatedTask = taskService.update(task, id);
        return (updatedTask == null)
                ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable (name = "id") int id){
        boolean deleted = taskService.delete(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
