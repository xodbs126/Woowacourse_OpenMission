package com.example.open_mission.user.dto;

import com.example.open_mission.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;

public record GetAllUsersResponse(Long id, String userName) {

    public static List<GetAllUsersResponse> fromUsers(List<User> users) {
        return users.stream()
                .map(user -> new GetAllUsersResponse(user.getId(), user.getUserName()))
                .collect(Collectors.toList());
    }
}
