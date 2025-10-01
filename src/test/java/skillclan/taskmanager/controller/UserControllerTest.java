package skillclan.taskmanager.controller;

import jdk.jfr.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skillclan.taskmanager.mapper.UserMapperImpl;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        user1.setEmail("test1@test.ua");

        User user2 = new User();
        user2.setId(10);
        user2.setName("TestUserName1");
        user2.setEmail("test1@test.ua");

        when(userService.create(user1)).thenReturn(user2);

        mockMvc.perform(post("/users")
                .content("""                      
                        {
                          "name": "TestUserName1",
                          "email": "test1@test.ua"
                        }
                        """)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "id": 10,
                          "name": "TestUserName1",
                          "email": "test1@test.ua"
                        }
                        """));

        verify(userService).create(user1);

    }
}
