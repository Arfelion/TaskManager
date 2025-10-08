package skillclan.taskmanager.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skillclan.taskmanager.dto.TaskDto;
import skillclan.taskmanager.mapper.TaskMapper;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper){
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto){
        final Task task = taskMapper.taskDtoToTask(taskDto);
        final Task createdTask = taskService.create(task);
        return new ResponseEntity<>(taskMapper.taskToTaskDto(createdTask), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        final List<Task> tasks = taskService.readAll();
        return new ResponseEntity<>(tasks.stream()
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable (name = "id") int id){
        final Task task = taskService.read(id);
        return (task == null) //Next time, this will be replaced with error handling
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(taskMapper.taskToTaskDto(task), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTaskById(@RequestBody TaskDto taskDto, @PathVariable (name = "id") int id){
        final Task task = taskMapper.taskDtoToTask(taskDto);
        final Task updatedTask = taskService.update(task, id);
        return (updatedTask == null) //Next time, this will be replaced with error handling
                ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(taskMapper.taskToTaskDto(updatedTask), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable (name = "id") int id){
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<TaskDto> assignTaskToUsers(@RequestBody int[] userIds, @PathVariable (name = "id") int taskId){
        Task task = taskService.assignTaskToUsers(taskId, userIds);
        return new ResponseEntity<>(taskMapper.taskToTaskDto(task), HttpStatus.OK);
    }
}
