package skillclan.taskmanager.service.impl;

import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.service.TaskService;

import java.util.List;

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
        boolean updated = taskRepository.update(task, id);
        task.setId(id);
        return updated ? task : null;
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }
}
