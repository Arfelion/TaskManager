package skillclan.taskmanager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skillclan.taskmanager.dto.UserDto;
import skillclan.taskmanager.mapper.UserMapper;
import skillclan.taskmanager.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    private List<User> USERS;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        USERS = null;

        userService.resetUsers(); //Костиль-метод очистки ХешМапи та скидання ІдГенератора

        user1 = new User();
        user1.setId(1);
        user1.setEmail("test1@test.com");
        user1.setName("TestUserName1");
        user1.setPhoneNumber("380120000001");

        user2 = new User();
        user2.setId(2);
        user2.setEmail("test2@test.com");
        user2.setName("TestUserName2");
        user2.setPhoneNumber("380120000002");
    }

    @Test
    void testCreateUser() {
        User createdUser = userService.create(user1);
        assertTrue(createdUser.getId() > 0);
        assertEquals("test1@test.com", createdUser.getEmail());
        assertEquals("TestUserName1", createdUser.getName());
        assertEquals("380120000001", createdUser.getPhoneNumber());
    }

    @Test
    void testReadAllUsers(){
        userService.create(user1); //no Create
        userService.create(user2); //no Create
        USERS = userService.readAll();
        assertNotNull(USERS);
        assertEquals(2, USERS.size());
    }

    @Test
    void testReadExistingUser() {
        User createdUser = userService.create(user1); //no Create
        User foundUser = userService.read(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(foundUser, createdUser);
        assertEquals("test1@test.com",createdUser.getEmail());
        assertEquals("TestUserName1",createdUser.getName());
        assertEquals("380120000001",createdUser.getPhoneNumber());
    }

    @Test
    void testReadNotExistingUser() {
        User foundUser = userService.read(Integer.MAX_VALUE);
        assertNull(foundUser);
    }

    @Test
    void testUpdateExistingUser() {
        User createdUser = userService.create(user1); //no Create
        int createdUserId = createdUser.getId();
        boolean results = userService.update(user2, createdUserId);
        User updatedUser = userService.read(createdUserId); //no Read
        assertTrue(results);
        assertEquals("test2@test.com", updatedUser.getEmail());
        assertEquals("TestUserName2", updatedUser.getName());
        assertEquals("380120000002", updatedUser.getPhoneNumber());
    }

    @Test
    void testUpdateNotExistingUser() {
        boolean results = userService.update(user1, userService.readAll().size() + 99);
        assertFalse(results);
        assertNull(userService.read(userService.readAll().size() + 99)); //no Read
    }

    @Test
    void testDeleteExistingUser(){
        User createdUser1 = userService.create(user1); //no Create
        User createdUser2 = userService.create(user2); //no Create
        int createdUserId1 = createdUser1.getId();
        int createdUserId2 = createdUser2.getId();
        int usersCountBeforeDelete = userService.readAll().size(); //no Read
        boolean result = userService.delete(createdUserId1);
        int usersCountAfterDelete = userService.readAll().size(); //no Read
        assertTrue(result);
        assertEquals(usersCountBeforeDelete - 1, usersCountAfterDelete);
        assertEquals(createdUser2, userService.read(createdUserId2)); //no Read
        assertNull(userService.read(createdUserId1)); //no Read
        assertNotNull(userService.read(createdUserId2)); //no Read
    }

    @Test
    void testDeleteNotExistingUser(){
        User createdUser1 = userService.create(user1); //no Create
        User createdUser2 = userService.create(user2); //no Create
        int createdUserId1 = createdUser1.getId();
        int createdUserId2 = createdUser2.getId();
        int usersCountBeforeDelete = userService.readAll().size();  //no Read
        boolean result = userService.delete(userService.readAll().size() + 99);  //no Read
        int usersCountAfterDelete = userService.readAll().size();  //no Read
        assertFalse(result);
        assertEquals(usersCountBeforeDelete, usersCountAfterDelete);
        assertEquals(createdUser1, userService.read(createdUserId1));  //no Read
        assertEquals(createdUser2, userService.read(createdUserId2));  //no Read
    }
}
