package uk.ac.bangor.cs.group2.academicymraeg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import uk.ac.bangor.cs.group2.academicymraeg.models.User;
import uk.ac.bangor.cs.group2.academicymraeg.repository.UserRepository;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_doesNotEncodePasswordIfAlreadyEncoded() {
        // Arrange
        User user = new User();
        user.setPasswordHash("$2a$alreadyEncodedPassword");
        user.setIsInstructor(User.IsInstructor.YES);
        user.setIsAdmin(User.IsAdmin.YES);

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals("$2a$alreadyEncodedPassword", savedUser.getPasswordHash());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUser_setsDefaultRolesIfNull() {
        // Arrange
        User user = new User();
        user.setPasswordHash("$2a$alreadyEncodedPassword");
        // isInstructor and isAdmin are null by default

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals(User.IsInstructor.NO, savedUser.getIsInstructor());
        assertEquals(User.IsAdmin.NO, savedUser.getIsAdmin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_returnsUser() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserByUsername_returnsUser() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUsername("testUser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void getAllUsers_returnsAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deleteUserById_deletesUser() {
        // Arrange & Act
        userService.deleteUserById(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void userExists_returnsTrueIfUserExists() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = userService.userExists(1L);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
    }
}
