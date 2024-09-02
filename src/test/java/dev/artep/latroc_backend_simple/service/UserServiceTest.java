package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.dto.LoginBody;
import dev.artep.latroc_backend_simple.dto.RegistrationBody;
import dev.artep.latroc_backend_simple.dto.UserResponse;
import dev.artep.latroc_backend_simple.exception.UserNotFoundException;
import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Post;
import dev.artep.latroc_backend_simple.repository.LocalPostDAO;
import dev.artep.latroc_backend_simple.repository.LocalUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private LocalUserDAO mockUserDAO;
    private LocalPostDAO mockPostDAO;
    private JWTService mockJWTService;

    @BeforeEach
    void setUp() {
        mockUserDAO = mock(LocalUserDAO.class);
        mockPostDAO = mock(LocalPostDAO.class);
        mockJWTService = mock(JWTService.class);
        userService = new UserService(mockUserDAO, mockPostDAO, mockJWTService);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("newUser");
        registrationBody.setEmail("newUser@example.com");
        registrationBody.setPassword("password");

        when(mockUserDAO.findByUsernameIgnoreCase("newUser")).thenReturn(Optional.empty());
        when(mockUserDAO.findByEmailIgnoreCase("newUser@example.com")).thenReturn(Optional.empty());
        when(mockUserDAO.save(any(LocalUser.class))).thenReturn(new LocalUser());

        LocalUser registeredUser = userService.registerUser(registrationBody);

        assertNotNull(registeredUser);
        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("newUser");
        verify(mockUserDAO, times(1)).findByEmailIgnoreCase("newUser@example.com");
        verify(mockUserDAO, times(1)).save(any(LocalUser.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("existingUser");
        registrationBody.setEmail("existingUser@example.com");
        registrationBody.setPassword("password");

        when(mockUserDAO.findByUsernameIgnoreCase("existingUser")).thenReturn(Optional.of(new LocalUser()));
        when(mockUserDAO.findByEmailIgnoreCase("existingUser@example.com")).thenReturn(Optional.of(new LocalUser()));

        assertThrows(Exception.class, () -> {
            userService.registerUser(registrationBody);
        });

        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("existingUser");
        verify(mockUserDAO, times(1)).findByEmailIgnoreCase("existingUser@example.com");
        verify(mockUserDAO, never()).save(any(LocalUser.class));
    }

    @Test
    void testLoginUser_Success() {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testUser");
        loginBody.setPassword("password");

        LocalUser user = new LocalUser();
        user.setUsername("testUser");
        user.setPassword("password");

        when(mockUserDAO.findByUsernameIgnoreCase("testUser")).thenReturn(Optional.of(user));
        when(mockJWTService.generateJWT(user)).thenReturn("mockToken");

        String token = userService.loginUser(loginBody);

        assertNotNull(token);
        assertEquals("mockToken", token);
        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("testUser");
        verify(mockJWTService, times(1)).generateJWT(user);
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testUser");
        loginBody.setPassword("wrongPassword");

        LocalUser user = new LocalUser();
        user.setUsername("testUser");
        user.setPassword("password");

        when(mockUserDAO.findByUsernameIgnoreCase("testUser")).thenReturn(Optional.of(user));

        String token = userService.loginUser(loginBody);

        assertNull(token);
        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("testUser");
        verify(mockJWTService, never()).generateJWT(any(LocalUser.class));
    }

    @Test
    void testGetUserById_Success() {
        LocalUser user = new LocalUser();
        user.setId(1L);
        when(mockUserDAO.findById(1L)).thenReturn(Optional.of(user));

        LocalUser foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(mockUserDAO, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(mockUserDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        verify(mockUserDAO, times(1)).findById(1L);
    }

    @Test
    void testGetUserIdByUsername_Success() {
        LocalUser user = new LocalUser();
        user.setId(1L);
        when(mockUserDAO.findByUsernameIgnoreCase("testUser")).thenReturn(Optional.of(user));

        Long userId = userService.getUserIdByUsername("testUser");

        assertNotNull(userId);
        assertEquals(1L, userId);
        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("testUser");
    }

    @Test
    void testGetUserIdByUsername_NotFound() {
        when(mockUserDAO.findByUsernameIgnoreCase("testUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.getUserIdByUsername("testUser");
        });

        verify(mockUserDAO, times(1)).findByUsernameIgnoreCase("testUser");
    }

    @Test
    void testGetUserDetailsWithPosts() {
        LocalUser user = new LocalUser();
        user.setId(1L);
        Post post1 = new Post();
        Post post2 = new Post();
        when(mockUserDAO.findById(1L)).thenReturn(Optional.of(user));
        when(mockPostDAO.findByUserId(1L)).thenReturn(Arrays.asList(post1, post2));

        UserResponse userResponse = userService.getUserDetailsWithPosts(1L);

        assertNotNull(userResponse);
        assertEquals(2, userResponse.getPosts().size());
        verify(mockUserDAO, times(1)).findById(1L);
        verify(mockPostDAO, times(1)).findByUserId(1L);
    }
}
