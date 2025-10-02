package skillclan.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.relational.core.sql.In;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skillclan.taskmanager.mapper.UserMapperImpl;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;
import skillclan.taskmanager.testutils.TestUser;

import java.util.ArrayList;
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

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testCreateUser_Success() throws Exception {
        User user1 = new User();
        user1.setName("TestUserName1");
        user1.setEmail("test1@test.test");

        User user2 = new User();
        user2.setId(10);
        user2.setName("TestUserName1");
        user2.setEmail("test1@test.test");

        when(userService.create(user1)).thenReturn(user2);

        mockMvc.perform(post("/users")
                .content("""                      
                        {
                          "name": "TestUserName1",
                          "email": "test1@test.test"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "name": "TestUserName1",
                          "email": "test1@test.test"
                        }
                        """));

        verify(userService).create(user1);

    }

    @Test
    void testFindById_Success() throws Exception {
        int ID = 10;

        when(userService.read(ID)).thenReturn(TestUser.getUser());

        mockMvc.perform(get("/users/" + ID)
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
    void testFindById_UnSuccess() throws Exception {
        int ID = Integer.MAX_VALUE;

        when(userService.read(ID)).thenReturn(null);

        mockMvc.perform(get("/users/" + Integer.MAX_VALUE)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
        verify(userService).read(ID);
    }

    @Test
    void testFindAll_Success() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(TestUser.getUser());

        when(userService.readAll()).thenReturn(users);

        mockMvc.perform(get("/users")
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
    void testFindAll_UnSuccess() throws Exception {
        List<User> users = new ArrayList<>();

        when(userService.readAll()).thenReturn(users);

        mockMvc.perform(get("/users")
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        []                        ]
                        """));
        verify(userService).readAll();

    }

    @Test
    void testUpdateUserById_Success() throws Exception {
        int ID = 10;
        User user = TestUser.getUserWithoutID();

        when(userService.update(user, ID)).thenReturn(TestUser.getUser());

        mockMvc.perform(put("/users/" + ID)
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
        verify(userService).update(user, ID);
    }

    @Test
    void testUpdateUserById_UnSuccess() throws Exception {
        int ID = Integer.MAX_VALUE;
        User user = TestUser.getUserWithoutID();

        when(userService.update(user, ID)).thenReturn(null);

        mockMvc.perform(put("/users/" + ID)
                .content("""                      
                        {
                          "name": "TestUserName",
                          "email": "test@test.test",
                          "phoneNumber": "380991234567"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
        verify(userService).update(user, ID);
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        int ID = 10;

        when(userService.delete(ID)).thenReturn(true);

        mockMvc.perform(delete("/users/" + ID)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserById_UnSuccess() throws Exception {
        int ID = Integer.MAX_VALUE;

        when(userService.delete(ID)).thenReturn(false);

        mockMvc.perform(delete("/users/" + ID)
                        .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound());
    }
}
