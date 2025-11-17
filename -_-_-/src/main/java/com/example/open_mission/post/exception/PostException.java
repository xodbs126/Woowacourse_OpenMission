package com.example.open_mission.post.exception;

import com.example.open_mission.common.domain.BaseException;
import com.example.open_mission.user.exception.UserErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostException extends BaseException {

    private final PostErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public int getStatus() {
        return errorCode.getStatus().value();
    }
}
