package com.example.open_mission.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateDto(

        @NotNull(message = "[ERROR] 사용자 ID는 필수입니다.")
        Long userId,

        @NotBlank(message = "[ERROR] 제목은 필수입니다.")
        @Size(max = 100, message = "[ERROR] 제목은 100자를 넘을 수 없습니다.") //
        String title,

        @NotBlank(message = "[ERROR] 내용은 필수입니다.")
        @Size(max = 1000, message = "[ERROR] 내용은 1000자를 넘을 수 없습니다.") //
        String content
) {
}