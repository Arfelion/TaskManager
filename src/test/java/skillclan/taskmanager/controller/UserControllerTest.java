package skillclan.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skillclan.taskmanager.exception.EntityNotFoundException;
import skillclan.taskmanager.mapper.UserMapperImpl;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;
import skillclan.taskmanager.testutils.user.TestUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(UserMapperImpl.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    private static final Integer ID = 10;
    private static final Integer NOT_EXISTING_ID = Integer.MAX_VALUE;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testCreateUser_Success() throws Exception {
        User requestUser = TestUser.getUserWithoutID();
        User createdUser = TestUser.getUser();

        when(userService.create(requestUser)).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/users")
                .content("""                      
                        {
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """));

        verify(userService).create(requestUser);

    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.read(ID)).thenReturn(TestUser.getUser());

        mockMvc.perform(get("/api/v1/users/{id}", ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """));
        verify(userService).read(ID);
    }

    @Test
    void testGetUserById_UnSuccess() throws Exception {
        when(userService.read(NOT_EXISTING_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/users/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
        verify(userService).read(NOT_EXISTING_ID);
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(TestUser.getUser());

        when(userService.readAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                          "id": 10,
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        ]
                        """));
        verify(userService).readAll();

    }

    @Test
    void testGetAllUsers_UnSuccess() throws Exception {
        when(userService.readAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/users")
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
        verify(userService).readAll();

    }

    @Test
    void testUpdateUserById_Success() throws Exception {
        User requestUser = TestUser.getUserWithoutID();
        User createdUser = TestUser.getUser();

        when(userService.update(requestUser, ID)).thenReturn(createdUser);

        mockMvc.perform(put("/api/v1/users/{ID}", ID)
                 .content("""                      
                        {
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """));
        verify(userService).update(requestUser, ID);
    }

    @Test
    void testUpdateUserById_UnSuccess() throws Exception {
        User user = TestUser.getUserWithoutID();

        when(userService.update(user, NOT_EXISTING_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/users/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
                .content("""                      
                        {
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
        verify(userService).update(user, NOT_EXISTING_ID);
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        when(userService.delete(ID)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/users/{ID}", ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserById_UnSuccess() throws Exception {
        when(userService.delete(NOT_EXISTING_ID)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/users/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNoContent());
    }
}
