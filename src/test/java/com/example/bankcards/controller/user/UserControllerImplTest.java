package com.example.bankcards.controller.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createNewUserValidRequestReturnsCreated() throws Exception {
        CreateUserRequestDto request = new CreateUserRequestDto(
                "testLogin", "Test User", "password123", Role.USER
        );
        CreateUserResponseDto response = new CreateUserResponseDto(
                "testLogin", "Test User", Role.USER
        );

        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testLogin"))
                .andExpect(jsonPath("$.userName").value("Test User"));

        verify(userService, times(1)).createUser(any(CreateUserRequestDto.class));
    }

    @Test
    void updateUserValidRequestReturnsUpdatedUser() throws Exception {
        UserDto request = new UserDto(1L, "updatedLogin", "Updated User", "newPass", Role.ADMIN);
        when(userService.updateUser(any(UserDto.class))).thenReturn(request);

        mockMvc.perform(patch("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("updatedLogin"));

        verify(userService, times(1)).updateUser(any(UserDto.class));
    }

    @Test
    void deleteUserValidIdReturnsSuccessMessage() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/user/delete/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted user with id" + userId));

        verify(userService, times(1)).deleteUser(eq(userId));
    }

    @Test
    void getUserByIdValidIdReturnsUser() throws Exception {
        Long userId = 1L;
        UserDto response = new UserDto(userId, "testLogin", "Test User", "password", Role.USER);
        when(userService.getUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/user/get/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("Test User"));

        verify(userService, times(1)).getUserById(eq(userId));
    }

    @Test
    void createNewUserInvalidRequestReturnsBadRequest() throws Exception {
        CreateUserRequestDto invalidRequest = new CreateUserRequestDto(
                "",
                "T",
                "",
                null
        );

        mockMvc.perform(post("/api/v1/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}