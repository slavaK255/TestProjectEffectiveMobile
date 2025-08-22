package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.WorkWithUserException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User createUser(Long id, String login, String userName, String password, Role role) {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setUserName(userName);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    private CreateUserRequestDto createUserRequestDto(String login, String userName, String password, Role role) {
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setLogin(login);
        dto.setUserName(userName);
        dto.setPassword(password);
        dto.setRole(role);
        return dto;
    }

    private UserDto createUserDto(Long id, String login, String userName, String password, Role role) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setLogin(login);
        dto.setUserName(userName);
        dto.setPassword(password);
        dto.setRole(role);
        return dto;
    }

    private CreateUserResponseDto createUserResponseDto() {
        CreateUserResponseDto dto = new CreateUserResponseDto();
        return dto;
    }

    @Test
    void findByLoginWhenUserExistsShouldReturnUser() {
        String login = "test_login";
        User user = createUser(1L, login, "Test User", "password", Role.USER);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        User result = userService.findByLogin(login);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
        verify(userRepository).findByLogin(login);
    }

    @Test
    void findByLoginWhenUserNotExistsShouldThrowException() {
        String login = "non_existent";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        WorkWithUserException exception = assertThrows(
                WorkWithUserException.class,
                () -> userService.findByLogin(login)
        );
        assertEquals("Пользователя не существует", exception.getMessage());
        verify(userRepository).findByLogin(login);
    }

    @Test
    void findByUserNameWhenUserExistsShouldReturnUser() {
        String userName = "John Doe";
        User user = createUser(1L, "john_doe", userName, "password", Role.USER);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        User result = userService.findByUserName(userName);

        assertNotNull(result);
        assertEquals(userName, result.getUserName());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void findByUserNameWhenUserNotExistsShouldThrowException() {
        String userName = "Non Existent";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        WorkWithUserException exception = assertThrows(
                WorkWithUserException.class,
                () -> userService.findByUserName(userName)
        );
        assertEquals("Пользователя не существует", exception.getMessage());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void createUserWithNewLoginShouldCreateUser() {
        CreateUserRequestDto requestDto = createUserRequestDto(
                "new_login", "New User", "password", Role.USER
        );

        User user = createUser(null,
                requestDto.getLogin(),
                requestDto.getUserName(),
                requestDto.getPassword(),
                requestDto.getRole()
        );

        User savedUser = createUser(null,
                requestDto.getLogin(),
                requestDto.getUserName(),
                requestDto.getPassword(),
                requestDto.getRole()
        );

        CreateUserResponseDto responseDto = createUserResponseDto();

        when(userRepository.existsByLogin(requestDto.getLogin())).thenReturn(false);
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toCreateUserResponseDto(savedUser)).thenReturn(responseDto);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn(requestDto.getPassword());

        CreateUserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        verify(userRepository).existsByLogin(requestDto.getLogin());
        verify(userRepository).save(user);
    }

    @Test
    void createUserWithExistingLoginShouldThrowException() {
        CreateUserRequestDto requestDto = createUserRequestDto(
                "existing_login", null, null, null
        );

        when(userRepository.existsByLogin(requestDto.getLogin())).thenReturn(true);

        WorkWithUserException exception = assertThrows(
                WorkWithUserException.class,
                () -> userService.createUser(requestDto)
        );
        assertEquals("Пользователя c таким логином уже существует", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserWithValidDataShouldUpdateFields() {
        UserDto userDto = createUserDto(
                1L, "updated_login", "Updated Name", "new_password", Role.ADMIN
        );

        User existingUser = createUser(
                1L, "old_login", "Old Name", "old_password", Role.USER
        );

        UserDto expectedDto = createUserDto(
                1L, "updated_login", "Updated Name", "new_password", Role.ADMIN
        );

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existingUser));
        when(userMapper.toDto(existingUser)).thenReturn(expectedDto);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn(userDto.getPassword());


        UserDto result = userService.updateUser(userDto);

        assertNotNull(result);
        assertEquals("updated_login", existingUser.getLogin());
        assertEquals("Updated Name", existingUser.getUserName());
        assertEquals("new_password", existingUser.getPassword());
        assertEquals(Role.ADMIN, existingUser.getRole());

        verify(userRepository).findById(userDto.getId());
    }

    @Test
    void updateUserWithPartialUpdateShouldUpdateOnlyNonNullFields() {
        UserDto userDto = createUserDto(1L, null, "Updated Name", null, null);
        User existingUser = createUser(1L, "old_login", "Old Name", "old_password", Role.USER);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existingUser));
        when(userMapper.toDto(existingUser)).thenReturn(userDto);

        UserDto result = userService.updateUser(userDto);

        assertNotNull(result);
        assertEquals("Updated Name", existingUser.getUserName());
        assertEquals("old_login", existingUser.getLogin());
        assertEquals("old_password", existingUser.getPassword());
        assertEquals(Role.USER, existingUser.getRole());
    }

    @Test
    void updateUserWhenUserNotFoundShouldThrowException() {
        UserDto userDto = createUserDto(99L, null, null, null, null);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        WorkWithUserException exception = assertThrows(
                WorkWithUserException.class,
                () -> userService.updateUser(userDto)
        );
        assertEquals("Пользователя не существует", exception.getMessage());
    }

    @Test
    void deleteUserShouldCallRepository() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void getUserByIdWhenUserExistsShouldReturnDto() {
        Long userId = 1L;
        User user = createUser(userId, "test", "Test User", "pass", Role.USER);
        UserDto userDto = createUserDto(userId, null, null, null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserByIdWhenUserNotExistsShouldThrowException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        WorkWithUserException exception = assertThrows(
                WorkWithUserException.class,
                () -> userService.getUserById(userId)
        );
        assertEquals("Пользователя не существует", exception.getMessage());
    }
}