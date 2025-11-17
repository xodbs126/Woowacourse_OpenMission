package com.example.open_mission.user.dto;

import jakarta.validation.constraints.NotNull;

public record UserCreateDto(
        @NotNull(message = "[ERROR] 사용자 이름은 필수입니다.")
        String userName) {
}
