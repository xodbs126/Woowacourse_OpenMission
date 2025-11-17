package com.example.open_mission.user.service;

import com.example.open_mission.user.dto.UserUpdateDto;
import com.example.open_mission.user.domain.User;
import com.example.open_mission.user.dto.UserCreateDto;
import com.example.open_mission.user.exception.UserErrorCode;
import com.example.open_mission.user.exception.UserException;
import com.example.open_mission.user.repository.UserJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;

    @Transactional
    @Override
    public User createUser(UserCreateDto userCreateDto) {
        String userName = userCreateDto.userName();

        checkDuplicatedUserName(userName);

        User user = User.createUser(userName);

        return userJpaRepository.save(user);
    }

    private void checkDuplicatedUserName(String userName) {
        if (userJpaRepository.existsByUserName(userName)) {
            throw new UserException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userJpaRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = getUserById(userId);

        String changedName = userUpdateDto.userName();
        checkDuplicatedUserName(changedName);
        User updatedUser = user.updateName(userUpdateDto.userName());

        return updatedUser;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userJpaRepository.delete(user);
    }

}
