package com.example.open_mission.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getMessage();

    String getCode();

    HttpStatus getStatus();
}
