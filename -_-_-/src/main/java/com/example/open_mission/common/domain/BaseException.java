package com.example.open_mission.common.domain;

public abstract class BaseException extends RuntimeException {

    public abstract String getMessage();

    public abstract String getCode();

    public abstract int getStatus();
}
