package skillclan.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skillclan.taskmanager.mapper.TaskMapperImpl;
import skillclan.taskmanager.model.Task;
import skillclan.taskmanager.service.TaskService;
import skillclan.taskmanager.testutils.task.TestTask;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TaskMapperImpl.class)
@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest {

    private static final Integer ID = 10;
    private static final Integer NOT_EXISTING_ID = Integer.MAX_VALUE;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void testCreateTask_Success() throws Exception {
        Task requestTask = TestTask.getTaskWithoutID();
        Task createdTask = TestTask.getTask();

        when(taskService.create(requestTask)).thenReturn(createdTask);

        mockMvc.perform(post("/api/v1/tasks")
                .content("""                      
                        {
                          "title": "TestTaskTitle",
                          "status": "NEW",
                          "description": "This is a description of the test task"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "title": "TestTaskTitle",
                          "status": "NEW",
                          "description": "This is a description of the test task"
                        }
                        """));

        verify(taskService).create(requestTask);
    }

    @Test
    void testGetAllTasks_Success() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(TestTask.getTask());

        when(taskService.readAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/tasks")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "id": 10,
                            "title": "TestTaskTitle",
                            "status": "NEW",
                            "description": "This is a description of the test task"
                          }
                        ]
                        """));

        verify(taskService).readAll();
    }

    @Test
    void testGetAllTasks_UnSuccess() throws Exception {
        List<Task> tasks = new ArrayList<>();

        when(taskService.readAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/tasks")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));

        verify(taskService).readAll();
    }

    @Test
    void testGetTaskById_Success() throws Exception {
        when(taskService.read(ID)).thenReturn(TestTask.getTask());

        mockMvc.perform(get("/api/v1/tasks/{ID}", ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 10,
                            "title": "TestTaskTitle",
                            "status": "NEW",
                            "description": "This is a description of the test task"
                          }
                        """));

        verify(taskService).read(ID);
    }

    @Test
    void testGetTaskById_UnSuccess() throws Exception {
        when(taskService.read(NOT_EXISTING_ID)).thenReturn(null);

        mockMvc.perform(get("/api/v1/tasks/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());

        verify(taskService).read(NOT_EXISTING_ID);
    }

    @Test
    void testUpdateTaskById_Success() throws Exception {
        Task requestTask = TestTask.getTaskWithoutID();
        Task createdTask = TestTask.getTask();

        when(taskService.update(requestTask, ID)).thenReturn(createdTask);

        mockMvc.perform(put("/api/v1/tasks/{ID}", ID)
                .content("""
                        {
                          "title": "TestTaskTitle",
                          "status": "NEW",
                          "description": "This is a description of the test task"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "title": "TestTaskTitle",
                          "status": "NEW",
                          "description": "This is a description of the test task"
                        }
                        """));

        verify(taskService).update(requestTask, ID);
    }

    @Test
    void testUpdateTaskById_UnSuccess() throws Exception {
        Task requestTask = TestTask.getTaskWithoutID();

        when(taskService.update(requestTask, NOT_EXISTING_ID)).thenReturn(null);

        mockMvc.perform(put("/api/v1/tasks/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
                        .content("""
                        {
                          "title": "TestTaskTitle",
                          "status": "NEW",
                          "description": "This is a description of the test task"
                        }
                        """)
                        .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());

        verify(taskService).update(requestTask, NOT_EXISTING_ID);
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        when(taskService.delete(ID)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tasks/{ID}", ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNoContent());

        verify(taskService).delete(ID);
    }

    @Test
    void testDeleteUserById_UnSuccess() throws Exception {
        when(taskService.delete(NOT_EXISTING_ID)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/tasks/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
                        .header("Content-Type", "application/json"))
                .andExpect(status().isNoContent());

        verify(taskService).delete(NOT_EXISTING_ID);
    }
}
