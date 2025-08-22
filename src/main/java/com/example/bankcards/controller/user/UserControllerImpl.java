package com.example.bankcards.controller.user;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.dto.create_user.CreateUserRequestDto;
import com.example.bankcards.dto.create_user.CreateUserResponseDto;
import com.example.bankcards.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @PostMapping("new")
    public CreateUserResponseDto createNewUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        return userService.createUser(createUserRequestDto);
    }

    @PatchMapping("update")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("delete/{userId}")
    public String deleteUser(@PathVariable @NotNull Long userId) {
        userService.deleteUser(userId);
        return "deleted user with id" + userId;
    }

    @GetMapping("get/{userId}")
    public UserDto getUserById(@PathVariable @NotNull Long userId){
        return userService.getUserById(userId);
    }
}
