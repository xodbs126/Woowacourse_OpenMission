package com.example.open_mission.user.exception;

import com.example.open_mission.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-001", "중복된 이름의 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-002", "사용자를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
