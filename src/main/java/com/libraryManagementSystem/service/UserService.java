package com.libraryManagementSystem.service;

import com.libraryManagementSystem.dto.*;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto register(UserRequest userRequest);

    LoginResponse login(LoginRequest loginRequest);

    Page<UserDto> getAllUsers(int pageNumber, int pageSize);

    UserDto getUserById(Long id);

    UserDto updateUser(UserDto user);

    void deleteUser(Long id);

    void changePassword(Long id, String newPassword, String newPasswordRepeat);
}
