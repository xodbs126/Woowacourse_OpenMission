package com.example.open_mission.user.service;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.example.open_mission.user.dto.UserUpdateDto;
import com.example.open_mission.user.domain.User;
import com.example.open_mission.user.dto.UserCreateDto;
import com.example.open_mission.user.exception.UserErrorCode;
import com.example.open_mission.user.exception.UserException;
import com.example.open_mission.user.repository.UserJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("사용자 생성")
    void 사용자_생성() {
        String userName = "newUser";
        UserCreateDto createDto = new UserCreateDto(userName);

        when(userJpaRepository.existsByUserName(userName)).thenReturn(false);

        User expectedUser = User.createUser(userName);
        when(userJpaRepository.save(any(User.class))).thenReturn(expectedUser);

        User createdUser = userService.createUser(createDto);

        assertThat(createdUser.getUserName()).isEqualTo(userName);

        verify(userJpaRepository, times(1)).existsByUserName(userName);
        verify(userJpaRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 사용자 생성")
    void 중복된_사용자_생성() {
        String existingUserName = "duplicateUser";
        UserCreateDto createDto = new UserCreateDto(existingUserName);

        when(userJpaRepository.existsByUserName(existingUserName)).thenReturn(true);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.createUser(createDto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USERNAME_ALREADY_EXISTS);

        verify(userJpaRepository, times(1)).existsByUserName(existingUserName);

        verify(userJpaRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("모든 사용자 조회")
    void 모든_사용자_조회() {
        User user1 = User.createUser("user1");
        User user2 = User.createUser("user2");
        when(userJpaRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        assertThat(users).containsExactlyInAnyOrder(user1, user2);

        verify(userJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("userId를 통한 사용자 조회")
    void userId를_통한_사용자_조회() {
        Long userId = 1L;
        User user = User.createUser("user1");
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertThat(foundUser).isEqualTo(user);

        verify(userJpaRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("사용자이름 수정")
    void 사용자이름_수정() {
        Long userId = 1L;
        User existingUser = User.createUser("user1");
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userJpaRepository.existsByUserName("user2")).thenReturn(false);

        User updatedUser = userService.updateUser(userId, new UserUpdateDto("user2"));

        assertThat(updatedUser.getUserName()).isEqualTo("user2");

        verify(userJpaRepository, times(1)).findById(userId);
        verify(userJpaRepository, times(1)).existsByUserName("user2");
    }

    @Test
    @DisplayName("중복된 이름으로 사용자이름 수정")
    void 중복된_이름_사용자_수정() {
        Long userId = 1L;
        User existingUser = User.createUser("user1");
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userJpaRepository.existsByUserName("user1")).thenReturn(true);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.updateUser(userId, new UserUpdateDto("user1"));
        });

        assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USERNAME_ALREADY_EXISTS);

        verify(userJpaRepository, times(1)).findById(userId);
        verify(userJpaRepository, times(1)).existsByUserName("user1");
    }

    @Test
    @DisplayName("사용자 삭제")
    void 사용자_삭제() {
        Long userId = 1L;
        User existingUser = User.createUser("user1");
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(userId);

        verify(userJpaRepository, times(1)).findById(userId);
        verify(userJpaRepository, times(1)).delete(existingUser);
    }

    @Test
    @DisplayName("사용자가 없는 경우 삭제")
    void 사용자가_없는_경우_삭제() {
        Long userId = 1L;
        when(userJpaRepository.findById(userId)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteUser(userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);

        verify(userJpaRepository, times(1)).findById(userId);
        verify(userJpaRepository, never()).delete(any(User.class));
    }
}