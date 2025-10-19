package skillclan.taskmanager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.model.TaskStatus;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.testutils.task.TestTask;
import skillclan.taskmanager.testutils.user.TestUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskServiceImpl taskService;
    private static final Integer ID = 10;
    private static final Integer NOT_EXISTING_ID = Integer.MAX_VALUE;

    @Test
    void testCreateTask(){
        Task requestTask = TestTask.getTaskWithoutID();
        Task createdTask = TestTask.getTask();

        when(taskRepository.create(requestTask)).thenReturn(Optional.of(createdTask));

        assertEquals(taskService.create(requestTask), createdTask);
        verify(taskRepository).create(requestTask);
    }

    @Test
    void testReadAllTasks(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(TestTask.getTask());

        when(taskRepository.findAll()).thenReturn(tasks);

        assertIterableEquals(tasks, taskService.readAll());
        verify(taskRepository).findAll();
    }

    @Test
    void testReadExistingTask(){
        when(taskRepository.findById(ID)).thenReturn(Optional.of(TestTask.getTaskWithoutID()));

        Task task = taskService.read(ID);

        assertNotNull(task);
        assertEquals(task, TestTask.getTaskWithoutID());
        verify(taskRepository).findById(ID);
    }

    @Test
    void testReadNoExistingTask(){
        when(taskRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertNull(taskService.read(NOT_EXISTING_ID));
        verify(taskRepository).findById(NOT_EXISTING_ID);
    }

    @Test
    void testUpdate_onlyUpdate(){
        Task requestTask = TestTask.getTaskWithoutID();
        requestTask.setAssignUsers(TestUser.getUsersList(1, 10));

        Task taskFromDB = TestTask.getTask();
        taskFromDB.setAssignUsers(TestUser.getUsersList(1, 10).reversed());
        taskFromDB.setStatus(TaskStatus.IN_PROGRESS);
        taskFromDB.setDescription(taskFromDB.getDescription() + "NEW");
        taskFromDB.setTitle(taskFromDB.getTitle() + "NEW");

        when(taskRepository.findById(ID)).thenReturn(Optional.of(taskFromDB));
        when(taskRepository.update(requestTask, ID)).thenReturn(true);

        Task updatedTask = taskService.update(requestTask, ID);

        assertEquals(ID, updatedTask.getId());
        assertEquals(requestTask.getTitle(), updatedTask.getTitle());
        assertEquals(requestTask.getDescription(), updatedTask.getDescription());
        assertEquals(requestTask.getStatus(), updatedTask.getStatus());
        verify(taskRepository).findById(ID);
        verify(taskRepository).update(requestTask, ID);
        verify(taskRepository, never()).assignTaskToUsers(anyInt(), anyList());
        verify(taskRepository, never()).keepOnlyTaskAssignees(anyInt(), anyList());
    }

    @Test
    void testUpdate_onlyAssign(){
        Task requestTask = TestTask.getTaskWithoutID();
        requestTask.setAssignUsers(TestUser.getUsersList(1, 4));

        Task taskFromDB = TestTask.getTask();
        taskFromDB.setAssignUsers(TestUser.getUsersList(2, 3));

        when(taskRepository.findById(ID)).thenReturn(Optional.of(taskFromDB));
        when(taskRepository.assignTaskToUsers(ID, List.of(1,2,3,4))).thenReturn(true);

        Task updatedTask = taskService.update(requestTask, ID);

        assertEquals(ID, updatedTask.getId());
        verify(taskRepository).findById(ID);
        verify(taskRepository, never()).update(any(Task.class), anyInt());
        verify(taskRepository).assignTaskToUsers(ID, List.of(1,2,3,4));
        verify(taskRepository, never()).keepOnlyTaskAssignees(anyInt(), anyList());
    }

    @Test
    void testUpdate_onlyUnassign(){
        Task requestTask = TestTask.getTaskWithoutID();
        requestTask.setAssignUsers(TestUser.getUsersList(2,3));

        Task taskFromDB = TestTask.getTask();
        taskFromDB.setAssignUsers(TestUser.getUsersList(1, 4));

        when(taskRepository.findById(ID)).thenReturn(Optional.of(taskFromDB));
        when(taskRepository.keepOnlyTaskAssignees(ID, List.of(2,3))).thenReturn(true);

        Task updatedTask = taskService.update(requestTask, ID);

        assertEquals(ID, updatedTask.getId());
        verify(taskRepository).findById(ID);
        verify(taskRepository, never()).update(any(Task.class), anyInt());
        verify(taskRepository, never()).assignTaskToUsers(anyInt(), anyList());
        verify(taskRepository).keepOnlyTaskAssignees(ID, List.of(2,3));
    }

    @Test
    void testUpdateNoExistingTask(){
        Task requestTask = TestTask.getTaskWithoutID();
        requestTask.setAssignUsers(TestUser.getUsersList(1, 5));

        when(taskRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        Task updatedTask = taskService.update(requestTask, NOT_EXISTING_ID);

        assertNull(updatedTask);
        verify(taskRepository).findById(NOT_EXISTING_ID);
        verify(taskRepository, times(0)).update(any(Task.class), anyInt());
        verify(taskRepository, times(0)).assignTaskToUsers(anyInt(), anyList());
        verify(taskRepository, times(0)).keepOnlyTaskAssignees(anyInt(), anyList());
    }

    @Test
    void testDeleteExistingTask(){
        when(taskRepository.delete(ID)).thenReturn(true);

        assertTrue(taskService.delete(ID));
    }

    @Test
    void testDeleteNoExistingTask(){
        when(taskRepository.delete(NOT_EXISTING_ID)).thenReturn(false);

        assertFalse(taskService.delete(NOT_EXISTING_ID));
    }
}

