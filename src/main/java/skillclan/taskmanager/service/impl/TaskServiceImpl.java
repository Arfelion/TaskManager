package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.repository.UserRepository;
import skillclan.taskmanager.repository.UserTaskRepository;
import skillclan.taskmanager.service.TaskService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserTaskRepository userTaskRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
        this.userRepository = userRepository;
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

    public Task assignTaskToUser(int taskId, int[] userIds) {
        Task task = taskRepository.findById(taskId).orElse(null);
        List<User> users = new ArrayList<>();
        if(task == null){
            return null;
        } else {
            for (int id : userIds) {
                if(userTaskRepository.assignTaskToUser(taskId, id)){
                    users.add(userRepository.findById(id).orElse(null));
                }
            }
            task.setAssignUsers(users);
        }
        return task;
    }
}
