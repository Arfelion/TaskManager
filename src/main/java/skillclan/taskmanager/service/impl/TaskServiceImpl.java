package skillclan.taskmanager.service.impl;

import org.springframework.stereotype.Service;
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
        Task partUpdatedTask = taskRepository.findById(id).orElse(null);
        if (partUpdatedTask == null){
            return null; // Якщо таску не знайшло = її не існує - нічого апдейтити не потрібно =)
        }
        if (partUpdatedTask.getStatus() != task.getStatus() ||
            !partUpdatedTask.getTitle().equals(task.getTitle()) ||
            !partUpdatedTask.getDescription().equals(task.getDescription())
        ){
            taskRepository.update(task, id); //Апдейтимо "звичайні" поля таски в БД якщо є зміни
        }
        Set<Integer> newUsersToAssign = task.getAssignUsers().stream().map(User::getId).collect(Collectors.toSet());
        List<Integer> oldAssignedUsers = partUpdatedTask.getAssignUsers().stream().map(User::getId).toList();
        /* Якщо є newUsersToAssign, які відсутні в oldAssignedUsers (з БД)
           і в результаті їх асайну (affectedRows <= 0) (таких користувачів не існує)
           повертаємо таску, яку ми отримали з БД */
        boolean isAssignedNewUsers;
        if (!oldAssignedUsers.containsAll(newUsersToAssign)){
            isAssignedNewUsers = taskRepository.assignTaskToUsers(id, new ArrayList<>(newUsersToAssign));
        }
        boolean isUnassignOldUsers = true;
        if (!newUsersToAssign.containsAll(oldAssignedUsers)) {
            isUnassignOldUsers = taskRepository.unassignOtherUsersFromTask(id, new ArrayList<>(newUsersToAssign));
        }
        //В залежності вір реалізації і того що ми хочемо віддати тут можуть бути різні варіанти (isAssignedNewUsers та isUnassignOldUsers - чи додали/видалили користувачів)
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
