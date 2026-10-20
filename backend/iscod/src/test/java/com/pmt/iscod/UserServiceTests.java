package com.pmt.iscod;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.pmt.iscod.repositories.UserRepository;
import com.pmt.iscod.services.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        when(userRepository.findByEmail("existing@example.com")).thenReturn(java.util.Optional.of(existingUser));
        User newUser = new User();
        newUser.setEmail("existing@example.com");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(newUser));
    }

    @Test
    void registerUser_ShouldReturnSavedUser() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = userService.registerUser(newUser);

        assertEquals("test@example.com", savedUser.getEmail());
        assertNotNull(savedUser);
    }

    @Test
    void loginUser_ShouldReturnUser_WhenCredentialsAreCorrect() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("secret");
        when(userRepository.findByEmail("user@example.com")).thenReturn(java.util.Optional.of(user));

        User result = userService.loginUser("user@example.com", "secret");

        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void loginUser_ShouldThrow_WhenPasswordIsWrong() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("correct");
        when(userRepository.findByEmail("user@example.com")).thenReturn(java.util.Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser("user@example.com", "wrong"));
    }

    @Test
    void loginUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.loginUser("unknown@example.com", "pass"));
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        User u1 = new User();
        User u2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }
}
