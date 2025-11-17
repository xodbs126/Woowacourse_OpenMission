package com.example.open_mission.user.dto;

public record GetAllUserResponse(Long userId, String userName) {
    public static GetAllUserResponse fromUser(com.example.open_mission.user.domain.User user) {
        return new GetAllUserResponse(user.getId(), user.getUserName());
    }
}
