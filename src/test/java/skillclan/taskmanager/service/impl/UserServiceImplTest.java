package skillclan.taskmanager.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private List<User> USERS;
    private User user1;
    private User user11;
    private User user2;


    @BeforeEach
    void setUp() {
        USERS = new ArrayList<>();

        user1 = new User();
        user1.setEmail("test1@test.com");
        user1.setName("TestUserName1");
        user1.setPhoneNumber("380120000001");

        user11 = new User();
        user11.setId(10);
        user11.setEmail("test1@test.com");
        user11.setName("TestUserName1");
        user11.setPhoneNumber("380120000001");

        user2 = new User();
        user2.setEmail("test2@test.com");
        user2.setName("TestUserName2");
        user2.setPhoneNumber("380120000002");
    }

    @Test
    void testCreateUser() {
        when(userRepository.create(user1)).thenReturn(Optional.of(user11));

        User createdUser = userService.create(user1);

        assertEquals(user11, createdUser);
        verify(userRepository).create(user1);
    }

    @Test
    void testReadAllUsers(){
        USERS.add(user1);
        USERS.add(user2);
        when(userRepository.findAll()).thenReturn(USERS);

        List<User> allUsers = userService.readAll();

        assertIterableEquals(USERS, allUsers);
        verify(userRepository).findAll();
    }

    @Test
    void testReadExistingUser() {
        when(userRepository.findById(10)).thenReturn(Optional.of(user1));

        User foundUser1 = userService.read(10);

        assertNotNull(foundUser1);
        assertEquals(foundUser1, user1);
        verify(userRepository).findById(10);
    }

    @Test
    void testReadNotExistingUser() {
        when(userRepository.findById(Integer.MAX_VALUE)).thenReturn(Optional.empty());

        User foundUser = userService.read(Integer.MAX_VALUE);

        assertNull(foundUser);
        verify(userRepository).findById(Integer.MAX_VALUE);
    }

    @Test
    void testFullUpdateExistingUser() {
        int ID = 10;
        when(userRepository.update(user2, ID)).thenReturn(true);

        User results = userService.update(user2, 10);

        assertEquals(10, results.getId());
        assertEquals("TestUserName2", results.getName());
        assertEquals("test2@test.com", results.getEmail());
        assertEquals("380120000002", results.getPhoneNumber());
        verify(userRepository).update(
                argThat(updatedUser -> {
                    boolean isNameCorrect = updatedUser.getName().equals(user2.getName());
                    boolean isEmailCorrect = updatedUser.getEmail().equals(user2.getEmail());
                    boolean isPhoneNumberCorrect = updatedUser.getPhoneNumber().equals(user2.getPhoneNumber());
                    return isNameCorrect && isEmailCorrect && isPhoneNumberCorrect;
                }), eq(ID));
    }


    @Test
    void testUpdateNotExistingUser() {
        when(userRepository.update(user1, Integer.MAX_VALUE)).thenReturn(false);
        User results = userService.update(user1, Integer.MAX_VALUE);
        assertNull(results);
        verify(userRepository).update(user1, Integer.MAX_VALUE);
    }

    @Test
    void testDeleteExistingUser(){
        int ID = 10;
        when(userRepository.delete(ID)).thenReturn(true);

        boolean result = userService.delete(ID);

        assertTrue(result);
    }

    @Test
    void testDeleteNotExistingUser(){
        int ID = Integer.MAX_VALUE;
        when(userRepository.delete(ID)).thenReturn(false);

        boolean result = userService.delete(ID);

        assertFalse(result);
    }
}
