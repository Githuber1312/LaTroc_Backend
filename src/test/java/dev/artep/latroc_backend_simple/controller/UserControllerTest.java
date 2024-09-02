package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.dto.LoginBody;
import dev.artep.latroc_backend_simple.dto.RegistrationBody;
import dev.artep.latroc_backend_simple.dto.LoginResponse;
import dev.artep.latroc_backend_simple.dto.UserResponse;
import dev.artep.latroc_backend_simple.service.JWTService;
import dev.artep.latroc_backend_simple.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jwtService = mock(JWTService.class);
        UserController userController = new UserController(userService, jwtService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testRegisterUser() throws Exception {
        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("newUser");
        registrationBody.setEmail("newUser@example.com");
        registrationBody.setPassword("password");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newUser\",\"email\":\"newUser@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).registerUser(any(RegistrationBody.class));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testUser");
        loginBody.setPassword("password");

        when(userService.loginUser(any(LoginBody.class))).thenReturn("mockToken");
        when(userService.getUserIdByUsername("testUser")).thenReturn(1L);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("mockToken"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(userService, times(1)).loginUser(any(LoginBody.class));
    }

    @Test
    void testLoginUser_InvalidCredentials() throws Exception {
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername("testUser");
        loginBody.setPassword("wrongPassword");

        when(userService.loginUser(any(LoginBody.class))).thenReturn(null);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).loginUser(any(LoginBody.class));
    }

    @Test
    void testGetUserById() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("testUser");

        when(userService.getUserDetailsWithPosts(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(userService, times(1)).getUserDetailsWithPosts(1L);
    }
}
