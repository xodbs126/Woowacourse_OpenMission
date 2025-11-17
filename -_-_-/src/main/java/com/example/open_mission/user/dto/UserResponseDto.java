package com.example.open_mission.user.dto;

import com.example.open_mission.user.exception.UserErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponseDto {

    private final int status;
    private final String code;
    private final String message;

    public static UserResponseDto success(HttpStatus status, String message) {
        return new UserResponseDto(status.value(), "", message);
    }

    public static UserResponseDto error(UserErrorCode errorCode) {
        return new UserResponseDto(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
    }

    private UserResponseDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}