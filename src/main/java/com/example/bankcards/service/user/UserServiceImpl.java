package com.example.bankcards.service.user;

import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.WorkWithUserException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new WorkWithUserException("Пользователя не существует"));
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new WorkWithUserException("Пользователя не существует"));
    }

    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if(userRepository.existsByLogin(createUserRequestDto.getLogin())){
            throw new WorkWithUserException("Пользователя c таким логином уже существует");
        }
        User user = userMapper.toEntity(createUserRequestDto);
        userRepository.save(user);
        return userMapper.toCreateUserResponseDto(user);
    }
}
