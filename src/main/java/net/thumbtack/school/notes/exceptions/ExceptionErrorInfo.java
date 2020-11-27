package net.thumbtack.school.notes.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionErrorInfo {

    LOGIN_ALREADY_EXISTS("User with this login already exists"),
    LOGIN_DOESNT_EXISTS("User with this login not registered on server"),
    USER_ALREADY_LOGGED_IN("You are already logged in");

    @Getter
    private final String errorString;
}
