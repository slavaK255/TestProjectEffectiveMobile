package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.WorkWithUserException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public User findByLogin(String login) {
        log.info("trying to find user by login : " + login);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new WorkWithUserException("Пользователя не существует"));
    }

    public User findByUserName(String userName) {
        log.info("trying to find user by user name : " + userName);

        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new WorkWithUserException("Пользователя не существует"));
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        log.info("Creating new user : " + createUserRequestDto.toString());

        if (userRepository.existsByLogin(createUserRequestDto.getLogin())) {
            throw new WorkWithUserException("Пользователя c таким логином уже существует");
        }

        User user = userMapper.toEntity(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        userRepository.save(user);

        return userMapper.toCreateUserResponseDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("update user details : " + userDto.toString());
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new WorkWithUserException("Пользователя не существует"));

        if (userDto.getUserName() != null) {
            user.setUserName(userDto.getUserName());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getRole() != null) {
            user.setRole(userDto.getRole());
        }
        if (userDto.getLogin() != null) {
            user.setLogin(userDto.getLogin());
        }

        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("remove user by id : " + userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("trying to find user by login : " + userId);
        return userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> new WorkWithUserException("Пользователя не существует")));
    }
}
