package com.example.open_mission.user.controller;

import com.example.open_mission.user.domain.User;
import com.example.open_mission.user.dto.GetAllUserResponse;
import com.example.open_mission.user.dto.GetAllUsersResponse;
import com.example.open_mission.user.dto.UserCreateDto;
import com.example.open_mission.user.dto.UserResponseDto;
import com.example.open_mission.user.dto.UserUpdateDto;
import com.example.open_mission.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        User createdUser = userService.createUser(userCreateDto);

        return ResponseEntity.ok(UserResponseDto.success(HttpStatus.OK, "사용자 생성이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<GetAllUsersResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(GetAllUsersResponse.fromUsers(users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetAllUserResponse> getUser(Long userId) {
        User user = userService.getUserById(userId);

        return ResponseEntity.ok(GetAllUserResponse.fromUser(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(userId, userUpdateDto);
        return ResponseEntity.ok(UserResponseDto.success(HttpStatus.OK, "사용자 정보가 업데이트 되었습니다."));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserResponseDto> deleteUser(Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(UserResponseDto.success(HttpStatus.OK, "사용자 삭제가 완료되었습니다."));
    }

}
