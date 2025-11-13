package skillclan.taskmanager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import skillclan.taskmanager.exception.EntityNotFoundException;
import skillclan.taskmanager.mapper.TaskMapper;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.service.NotificationProducer;
import skillclan.taskmanager.service.TaskService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final NotificationProducer producer;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, NotificationProducer producer, TaskMapper taskMapper){
        this.taskRepository = taskRepository;
        this.producer = producer;
        this.taskMapper = taskMapper;
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
        return taskRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Task with id=" + id + " was not found"));
    }

    @Override
    public Task update(Task task, int id) {
        Task taskFromDB = taskRepository.findById(id).orElse(null);
        if (taskFromDB == null) {
            throw new EntityNotFoundException("Task with id=" + id + " was not found");
        }
        boolean updated = false;

        if (taskFromDB.getStatus() != task.getStatus() ||
            !taskFromDB.getTitle().equals(task.getTitle()) ||
            !taskFromDB.getDescription().equals(task.getDescription())){
            taskRepository.update(task, id);
            updated = true;
            logger.info("Внесені зміни в ТаскІД {}. Відправляємо повідомлення?: {}", id, updated);
        }
        Set<Integer> newUsersToAssign = task.getAssignUsers().stream().map(User::getId).collect(Collectors.toSet());
        List<Integer> oldAssignedUsers = taskFromDB.getAssignUsers().stream().map(User::getId).toList();
        if (!oldAssignedUsers.containsAll(newUsersToAssign)) {
            taskRepository.assignTaskToUsers(id, new ArrayList<>(newUsersToAssign));
            updated = true;
            logger.info("Назначення користувачів у Таску. Відправляємо повідомлення?: {}", updated);
        }

        if (!newUsersToAssign.containsAll(oldAssignedUsers)) {
            taskRepository.keepOnlyTaskAssignees(id, new ArrayList<>(newUsersToAssign));
            updated = true;
            logger.info("Видалення зайвих користувачів з Таски. Відправляємо повідомлення?: {}", updated);
        }
        task.setId(id);
        logger.info("Повідомлення буде надіслано в RabbitMQ?: {}", updated);
        if(updated){
            producer.sendMessage(taskMapper.taskNotificationDto(task));
        }
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
