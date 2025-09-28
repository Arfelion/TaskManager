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
    UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private List<User> USERS;
    private User user1;
    private User user11;
    private User user2;
    private User userN;
    private User userE;
    private User userPN;


    @BeforeEach
    void setUp() {
        USERS = new ArrayList<>();

        userN = new User();
        userN.setName("NewTestUserName");

        userE = new User();
        userE.setEmail("NEWtest@test.com");

        userPN = new User();
        userPN.setPhoneNumber("380991234567");

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
        verify(userRepository, times(1)).create(user1);
    }

    @Test
    void testReadAllUsers(){
        USERS.add(user1);
        USERS.add(user2);
        when(userRepository.findAll()).thenReturn(USERS);

        List<User> allUsers = userService.readAll();

        assertEquals(USERS, allUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testReadExistingUser() {
        when(userRepository.findById(10)).thenReturn(Optional.of(user1));

        User foundUser1 = userService.read(10);

        assertNotNull(foundUser1);
        assertEquals(foundUser1, user1);
        verify(userRepository, times(1)).findById(10);
    }

    @Test
    void testReadNotExistingUser() {
        when(userRepository.findById(Integer.MAX_VALUE)).thenReturn(Optional.empty());

        User foundUser = userService.read(Integer.MAX_VALUE);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(Integer.MAX_VALUE);
    }

    @Test
    void testFullUpdateExistingUser() {
        int ID = 10;
        when(userRepository.update(user2, ID)).thenReturn(true);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user11));

        boolean results = userService.update(user2, 10);

        assertTrue(results);
        verify(userRepository, times(1)).findById(ID);
        verify(userRepository, times(1)).update(
                argThat(updatedUser -> {
                    boolean isNameCorrect = updatedUser.getName().equals(user2.getName());
                    boolean isEmailCorrect = updatedUser.getEmail().equals(user2.getEmail());
                    boolean isPhoneNumberCorrect = updatedUser.getPhoneNumber().equals(user2.getPhoneNumber());
                    return isNameCorrect && isEmailCorrect && isPhoneNumberCorrect;
                }), eq(ID));
    }

    @Test
    void testPartUpdateExistingUser() {
        int ID = 10;
        when(userRepository.update(any(), eq(ID))).thenReturn(true);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user11));

        // Only Name
        boolean resultsN = userService.update(userN, 10);
        assertTrue(resultsN);
        verify(userRepository, times(1)).findById(ID);
        verify(userRepository, times(1)).update(
                argThat(updatedUser -> {
                    boolean isNameCorrect = updatedUser.getName().equals(userN.getName());
                    boolean isEmailCorrect = updatedUser.getEmail().equals(user11.getEmail());
                    boolean isPhoneNumberCorrect = updatedUser.getPhoneNumber().equals(user11.getPhoneNumber());
                    return isNameCorrect && isEmailCorrect && isPhoneNumberCorrect;
                }), eq(ID));

        // Only Email
        boolean resultsE = userService.update(userE, 10);
        assertTrue(resultsE);
        verify(userRepository, times(2)).findById(ID);
        verify(userRepository, times(1)).update(
                argThat(updatedUser -> {
                    boolean isNameCorrect = updatedUser.getName().equals(user11.getName());
                    boolean isEmailCorrect = updatedUser.getEmail().equals(userE.getEmail());
                    boolean isPhoneNumberCorrect = updatedUser.getPhoneNumber().equals(user11.getPhoneNumber());
                    return isNameCorrect && isEmailCorrect && isPhoneNumberCorrect;
                }), eq(ID));

        // Only PhoneNumber
        boolean resultsPN = userService.update(userPN, 10);
        assertTrue(resultsPN);
        verify(userRepository, times(3)).findById(ID);
        verify(userRepository, times(1)).update(
                argThat(updatedUser -> {
                    boolean isNameCorrect = updatedUser.getName().equals(user11.getName());
                    boolean isEmailCorrect = updatedUser.getEmail().equals(user11.getEmail());
                    boolean isPhoneNumberCorrect = updatedUser.getPhoneNumber().equals(userPN.getPhoneNumber());
                    return isNameCorrect && isEmailCorrect && isPhoneNumberCorrect;
                }), eq(ID));
    }

    @Test
    void testUpdateNotExistingUser() {
        when(userRepository.findById(Integer.MAX_VALUE)).thenReturn(Optional.empty());
        boolean results = userService.update(user1, Integer.MAX_VALUE);
        assertFalse(results);
        verify(userRepository, times(1)).findById(Integer.MAX_VALUE);
        verify(userRepository, times(0)).update(any(), eq(Integer.MAX_VALUE));
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
