package net.thumbtack.school.notes.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ExceptionErrorInfo {

    LOGIN_ALREADY_EXISTS("User with this login already exists"),
    LOGIN_DOES_NOT_EXISTS("User with this login not registered on server"),
    USER_ALREADY_LOGGED_IN("You are already logged in"),
    USER_DOES_NOT_EXISTS("No such user on the server"),
    USER_IS_NOT_LOGGED_IN("You are not logged in"),
    SESSION_DOES_NOT_EXISTS("No such session on the server"),
    WRONG_PASSWORD("Wrong password");

    @Getter
    String errorString;
}
