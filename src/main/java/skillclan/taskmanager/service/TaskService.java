package skillclan.taskmanager.service;

import skillclan.taskmanager.model.Task;

import java.util.List;

public interface TaskService {

    Task create(Task task);

    List<Task> readAll();

    Task read(int id);

    Task update (Task task, int id);

    boolean delete(int id);

    Task assignTaskToUsers(int taskId, int[] userIds);
}
