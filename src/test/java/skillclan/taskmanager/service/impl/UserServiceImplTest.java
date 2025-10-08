package skillclan.taskmanager.service.impl;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skillclan.taskmanager.model.User;
import skillclan.taskmanager.repository.UserRepository;
import skillclan.taskmanager.testutils.user.TestUser;

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
    private static final Integer ID = 10;
    private static final Integer NOT_EXISTING_ID = Integer.MAX_VALUE;

    @Test
    void testCreateUser() {
        User requestUser = TestUser.getUserWithoutID();
        User createdUser = TestUser.getUser();

        when(userRepository.create(requestUser)).thenReturn(Optional.of(createdUser));

        assertEquals(userService.create(requestUser), createdUser);
        verify(userRepository).create(requestUser);
    }

    @Test
    void testReadAllUsers(){
        List<User> USERS = new ArrayList<>();
        User user = TestUser.getUser();
        USERS.add(user);

        when(userRepository.findAll()).thenReturn(USERS);

        List<User> allUsers = userService.readAll();

        assertIterableEquals(USERS, allUsers);
        verify(userRepository).findAll();
    }

    @Test
    void testReadExistingUser() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(TestUser.getUserWithoutID()));

        User foundUser = userService.read(ID);

        assertNotNull(foundUser);
        assertEquals(foundUser, TestUser.getUserWithoutID());
        verify(userRepository).findById(ID);
    }

    @Test
    void testReadNotExistingUser() {
        when(userRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        User foundUser = userService.read(NOT_EXISTING_ID);

        assertNull(foundUser);
        verify(userRepository).findById(NOT_EXISTING_ID);
    }

    @Test
    void testFullUpdateExistingUser() {
        User user = TestUser.getUserWithoutID();

        when(userRepository.update(user, ID)).thenReturn(true);

        User results = userService.update(user, ID);

        assertEquals(10, results.getId());
        assertEquals("TestUserName", results.getName());
        assertEquals("test@test.test", results.getEmail());
        assertEquals("380991234567", results.getPhoneNumber());
        verify(userRepository).update(
                argThat(updatedUser -> {
                    boolean isNameCorrect = updatedUser.getName().equals(user.getName());
                    boolean isEmailCorrect = updatedUser.getEmail().equals(user.getEmail());
                    boolean isPhoneNumberCorrect = updatedUser.getPhoneNumber().equals(user.getPhoneNumber());
                    return isNameCorrect && isEmailCorrect && isPhoneNumberCorrect;
                }), eq(ID));
    }


    @Test
    void testUpdateNotExistingUser() {
        User user = TestUser.getUserWithoutID();

        when(userRepository.update(user, NOT_EXISTING_ID)).thenReturn(false);
        User results = userService.update(user, NOT_EXISTING_ID);
        assertNull(results);
        verify(userRepository).update(user, NOT_EXISTING_ID);
    }

    @Test
    void testDeleteExistingUser(){
        when(userRepository.delete(ID)).thenReturn(true);

        boolean result = userService.delete(ID);

        assertTrue(result);
    }

    @Test
    void testDeleteNotExistingUser(){
        when(userRepository.delete(NOT_EXISTING_ID)).thenReturn(false);

        boolean result = userService.delete(NOT_EXISTING_ID);

        assertFalse(result);
    }
}
