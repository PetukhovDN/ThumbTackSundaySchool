package net.thumbtack.school.notes.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionErrorInfo {

    LOGIN_ALREADY_EXISTS("User with this login already exists");

    @Getter
    private final String errorString;
}
