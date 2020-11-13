package net.thumbtack.school.notes.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionErrorInfo {

    LOGIN_ALREADY_EXISTS("User already exists");

    @Getter
    private final String errorString;
}
