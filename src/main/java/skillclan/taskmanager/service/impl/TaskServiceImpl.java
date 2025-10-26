package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import skillclan.taskmanager.exception.TaskNotFoundException;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.service.TaskService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task) {
        return taskRepository.create(task).orElse(null);
    }

    @Override
    public List<Task> readAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task read(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task update(Task task, int id) {
        Task taskFromDB = taskRepository.findById(id).orElse(null);
        if (taskFromDB == null){
            throw new TaskNotFoundException("Task with id=" + id + " was not found");
        }
        if (taskFromDB.getStatus() != task.getStatus() ||
            !taskFromDB.getTitle().equals(task.getTitle()) ||
            !taskFromDB.getDescription().equals(task.getDescription())
        ){
            taskRepository.update(task, id);
        }
        Set<Integer> newUsersToAssign = task.getAssignUsers().stream().map(User::getId).collect(Collectors.toSet());
        List<Integer> oldAssignedUsers = taskFromDB.getAssignUsers().stream().map(User::getId).toList();
        if (!oldAssignedUsers.containsAll(newUsersToAssign)){
            taskRepository.assignTaskToUsers(id, new ArrayList<>(newUsersToAssign));
        }
        if (!newUsersToAssign.containsAll(oldAssignedUsers)) {
            taskRepository.keepOnlyTaskAssignees(id, new ArrayList<>(newUsersToAssign));
        }
        task.setId(id);
        return  task;
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    public Task assignTaskToUsers(int taskId, int[] userIds) {
        List<Integer> integerUserIds = Arrays.stream(userIds).boxed().collect(Collectors.toList());
        return taskRepository.assignTaskToUsers(taskId, integerUserIds) // тут проблема. Якщо юзери вже назначені на таски раніше то поверне null - потрібно обговорити
                ? taskRepository.findById(taskId).orElse(null)
                : null;
    }
}
