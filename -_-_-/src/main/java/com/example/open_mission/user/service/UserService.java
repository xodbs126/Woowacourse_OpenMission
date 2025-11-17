package com.example.open_mission.user.service;

import com.example.open_mission.user.dto.UserUpdateDto;
import com.example.open_mission.user.domain.User;
import com.example.open_mission.user.dto.UserCreateDto;
import java.util.List;

public interface UserService {
    User createUser(UserCreateDto userCreateDto);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User updateUser(Long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long userId);
}
