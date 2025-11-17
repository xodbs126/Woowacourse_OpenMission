package com.example.open_mission.post.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "게시물을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
