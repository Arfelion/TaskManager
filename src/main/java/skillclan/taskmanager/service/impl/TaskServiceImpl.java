package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.repository.UserTaskRepository;
import skillclan.taskmanager.service.TaskService;

import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserTaskRepository userTaskRepository){
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
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
        boolean updated = taskRepository.update(task, id);
        task.setId(id);
        return updated ? task : null;
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    public Task assignTaskToUsers(int taskId, int[] userIds) {
        Integer[] integerUserIds = Arrays.stream(userIds).boxed().toArray(Integer[]::new);
        return userTaskRepository.assignTaskToUsers(taskId, integerUserIds).orElse(null);
    }
}
