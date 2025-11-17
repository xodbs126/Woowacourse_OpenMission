package com.example.open_mission.post.dto;

import com.example.open_mission.post.exception.PostErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponseDto {

    private final int status;
    private final String code;
    private final String message;

    public static PostResponseDto success(HttpStatus status, String message) {
        return new PostResponseDto(status.value(), "", message);
    }

    public static PostResponseDto error(PostErrorCode errorCode) {
        return new PostResponseDto(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
    }

    private PostResponseDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}