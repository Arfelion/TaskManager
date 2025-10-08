package skillclan.taskmanager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.repository.TaskRepository;
import skillclan.taskmanager.testutils.task.TestTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
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
    void testUpdateExistingTask(){
        Task requestTask = TestTask.getTaskWithoutID();

        when(taskRepository.update(requestTask, ID)).thenReturn(true);

        Task updatedTask = taskService.update(requestTask, ID);

        assertEquals(ID, updatedTask.getId());
        assertEquals(requestTask.getTitle(), updatedTask.getTitle());
        assertEquals(requestTask.getDescription(), updatedTask.getDescription());
        assertEquals(requestTask.getStatus(), updatedTask.getStatus());
        verify(taskRepository).update(
                argThat(task -> {
                    boolean isTitleCorrect = updatedTask.getTitle().equals(requestTask.getTitle());
                    boolean isDescriptionCorrect = updatedTask.getDescription().equals(requestTask.getDescription());
                    boolean isStatusCorrect = updatedTask.getStatus().equals(requestTask.getStatus());
                    return isTitleCorrect && isDescriptionCorrect && isStatusCorrect;
                }), eq(ID));
    }

    @Test
    void testUpdateNoExistingTask(){
        Task requestTask = TestTask.getTaskWithoutID();

        when(taskRepository.update(requestTask, NOT_EXISTING_ID)).thenReturn(false);

        Task updatedTask = taskService.update(requestTask, NOT_EXISTING_ID);

        assertNull(updatedTask);
        verify(taskRepository).update(requestTask, NOT_EXISTING_ID);
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

