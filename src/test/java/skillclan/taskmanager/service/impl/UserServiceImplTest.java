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
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private List<UserDto> USERSDTO;
    private List<User> USERS;
    private UserDto userDto1;
    private UserDto userDto2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        USERSDTO = null;
        USERS = null;

        userDto1 = new UserDto();
        userDto1.setId(1);
        userDto1.setEmail("test1@test.com");
        userDto1.setName("TestUserName1");
        userDto1.setPhoneNumber("380120000001");

        userDto2 = new UserDto();
        userDto2.setId(2);
        userDto2.setEmail("test2@test.com");
        userDto2.setName("TestUserName2");
        userDto2.setPhoneNumber("380120000002");

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
        when(userMapper.userDtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        UserDto createdUserDto = userService.create(userDto1);
        assertTrue(createdUserDto.getId() > 0);
        assertEquals("test1@test.com", createdUserDto.getEmail());
        assertEquals("TestUserName1", createdUserDto.getName());
        assertEquals("380120000001", createdUserDto.getPhoneNumber());
    }

    @Test
    void testReadAllUsersWhenEmpty() {
        USERSDTO = userService.readAll();
        assertNotNull(USERSDTO);
        assertTrue(USERSDTO.isEmpty());
    }

    @Test
    void testReadAllUsersWhenNotEmpty(){
        when(userMapper.userDtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userDtoToUser(userDto2)).thenReturn(user2);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        userService.create(userDto1);
        userService.create(userDto2);
        USERSDTO = userService.readAll();
        assertNotNull(USERSDTO);
        assertEquals(2, USERSDTO.size());
    }

    @Test
    void testReadExistingUser() {
        when(userMapper.userDtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        UserDto createdUser = userService.create(userDto1);
        UserDto foundUser = userService.read(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(foundUser, createdUser);
        assertEquals("test1@test.com",createdUser.getEmail());
        assertEquals("TestUserName1",createdUser.getName());
        assertEquals("380120000001",createdUser.getPhoneNumber());
    }

    @Test
    void testReadNotExistingUser() {
        UserDto foundUser = userService.read(userService.readAll().size() + 99);
        assertNull(foundUser);
    }

    @Test
    void testUpdateExistingUser() {
        when(userMapper.userDtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userDtoToUser(userDto2)).thenReturn(user2);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        UserDto createdUser = userService.create(userDto1);
        int createdUserId = createdUser.getId();
        boolean results = userService.update(userDto2, createdUserId);
        UserDto updatedUser = userService.read(createdUserId);
        assertTrue(results);
        assertEquals("test2@test.com", updatedUser.getEmail());
        assertEquals("TestUserName2", updatedUser.getName());
        assertEquals("380120000002", updatedUser.getPhoneNumber());
    }

    @Test
    void testUpdateNotExistingUser() {
        boolean results = userService.update(userDto1, userService.readAll().size() + 99);
        assertFalse(results);
        assertNull(userService.read(userService.readAll().size() + 99));
    }

    @Test
    void testDeleteExistingUser(){
        when(userMapper.userDtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userDtoToUser(userDto2)).thenReturn(user2);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        UserDto createdUser1 = userService.create(userDto1);
        UserDto createdUser2 = userService.create(userDto2);
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
        when(userMapper.userDtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userDtoToUser(userDto2)).thenReturn(user2);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);
        UserDto createdUser1 = userService.create(userDto1);
        UserDto createdUser2 = userService.create(userDto2);
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
