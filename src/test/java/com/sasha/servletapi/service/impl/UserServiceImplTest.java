package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.util.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.sasha.servletapi.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(PER_METHOD)
class UserServiceImplTest extends UtilService {

    @Test
    void shouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(expectedUserWithIdName);

        User savedUser = userService.save(userWithName);

        assertNotNull(savedUser);
        assertEquals(expectedUserWithIdName, savedUser);
        assertEquals(userId, savedUser.getId());

        verify(userRepository, times(TEST_NUMBER_1)).save(any(User.class));
    }
    @Test
    void shouldUpdateDeveloper() {
        when(userRepository.findById(userId)).thenReturn(expectedUserWithIdName);
        when(userRepository.update(any(User.class))).thenReturn(expectedUserWithIdName);

        User updatedUser = userService.update(expectedUserWithIdName);

        assertNotNull(updatedUser);
        assertEquals(expectedUserWithIdName, updatedUser);

        verify(userRepository, times(TEST_NUMBER_1)).update(expectedUserWithIdName);
        verify(userRepository, times(TEST_NUMBER_1)).findById(userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdateUserAndUserNotExists() {
        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.update(expectedUserWithIdName));

        verify(userRepository, times(TEST_NUMBER_0)).update(expectedUserWithIdName);
        verify(userRepository, times(TEST_NUMBER_1)).findById(userId);
    }

    @Test
    void shouldFindByIdUser() {
        when(userRepository.findById(userId)).thenReturn(expectedUserWithIdName);

        User foundUser = userService.findById(userId);

        assertNotNull(foundUser);
        assertEquals(expectedUserWithIdName, foundUser);

        verify(userRepository, times(TEST_NUMBER_1)).findById(userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindByIdUserAndUserNotExists() {
        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.findById(userId));

        verify(userRepository, times(TEST_NUMBER_1)).findById(userId);
    }

    @Test
    void shouldFindAllUsers() {
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> foundUsers = userService.findAll();

        assertNotNull(foundUsers);
        assertFalse(foundUsers.isEmpty());
        assertEquals(expectedUsers, foundUsers);
        assertEquals(TEST_NUMBER_2, foundUsers.size());

        verify(userRepository, times(TEST_NUMBER_1)).findAll();
    }

    @Test
    void shouldDeleteByIdUser() {
        when(userRepository.findById(userId)).thenReturn(expectedUserWithIdName);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteById(userId);

        verify(userRepository, times(TEST_NUMBER_1)).findById(userId);
        verify(userRepository, times(TEST_NUMBER_1)).deleteById(userId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteUserByIdAndUserNotExists() {
        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.deleteById(userId));

        verify(userRepository, times(TEST_NUMBER_1)).findById(userId);
        verify(userRepository, times(TEST_NUMBER_0)).deleteById(userId);
    }

    @Test
    void shouldDeleteAllUsers() {
        doNothing().when(userRepository).deleteAll();

        userService.deleteAll();

        verify(userRepository, times(TEST_NUMBER_1)).deleteAll();
    }
}