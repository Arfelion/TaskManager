package skillclan.taskmanager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import skillclan.taskmanager.model.User;



import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceImplTest {

    private UserServiceImpl userService;
    private List<User> USERS;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
        USERS = null;
        user1 = new User();
        user1.setEmail("test1@test.com");
        user1.setName("TestUserName1");
        user1.setPhoneNumber("380120000001");

        user2 = new User();
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
    void testReadAllUsersWhenEmpty() {
        userService = new UserServiceImpl();
        USERS = userService.readAll();
        assertNotNull(USERS);
        assertTrue(USERS.isEmpty());
    }

    @Test
    void testReadAllUsersWhenNotEmpty(){
        userService.create(user1);
        userService.create(user2);
        USERS = userService.readAll();
        assertNotNull(USERS);
        assertEquals(2, USERS.size());
    }

    @Test
    void testReadExistingUser() {
        User createdUser = userService.create(user1);
        User foundUser = userService.read(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(foundUser, createdUser);
        assertEquals("test1@test.com",createdUser.getEmail());
        assertEquals("TestUserName1",createdUser.getName());
        assertEquals("380120000001",createdUser.getPhoneNumber());
    }

    @Test
    void testReadNotExistingUser() {
        User foundUser = userService.read(userService.readAll().size() + 99);
        assertNull(foundUser);
    }

    @Test
    void testUpdateExistingUser() {
        User createdUser = userService.create(user1);
        int createdUserId = createdUser.getId();
        boolean results = userService.update(user2, createdUserId);
        User updatedUser = userService.read(createdUserId);
        assertTrue(results);
        assertEquals(createdUserId, updatedUser.getId());
        assertEquals("test2@test.com", updatedUser.getEmail());
        assertEquals("TestUserName2", updatedUser.getName());
        assertEquals("380120000002", updatedUser.getPhoneNumber());
    }

    @Test
    void testUpdateNotExistingUser() {
        boolean results = userService.update(user1, userService.readAll().size() + 99);
        assertFalse(results);
        assertNull(userService.read(userService.readAll().size() + 99));
    }

    @Test
    void testDeleteExistingUser(){
        User createdUser1 = userService.create(user1);
        User createdUser2 = userService.create(user2);
        int createdUserId1 = createdUser1.getId();
        int createdUserId2 = createdUser2.getId();
        int usersCountBeforeDelete = userService.readAll().size();
        boolean result = userService.delete(createdUserId1);
        int usersCountAfterDelete = userService.readAll().size();
        assertTrue(result);
        assertEquals(usersCountBeforeDelete - 1, usersCountAfterDelete);
        assertEquals(createdUser2, userService.read(createdUserId2));
        assertNull(userService.read(createdUserId1));
        assertNotNull(userService.read(createdUserId2));
    }

    @Test
    void testDeleteNotExistingUser(){
        User createdUser1 = userService.create(user1);
        User createdUser2 = userService.create(user2);
        int createdUserId1 = createdUser1.getId();
        int createdUserId2 = createdUser2.getId();
        int usersCountBeforeDelete = userService.readAll().size();
        boolean result = userService.delete(userService.readAll().size() + 99);
        int usersCountAfterDelete = userService.readAll().size();
        assertFalse(result);
        assertEquals(usersCountBeforeDelete, usersCountAfterDelete);
        assertEquals(createdUser1, userService.read(createdUserId1));
        assertEquals(createdUser2, userService.read(createdUserId2));
    }
}
