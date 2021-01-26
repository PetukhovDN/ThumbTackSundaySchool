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
    USER_ACCOUNT_IS_DELETED("You have left the server"),
    SESSION_DOES_NOT_EXISTS("No such session on the server"),
    WRONG_PASSWORD("Wrong password"),
    SESSION_EXPIRED("Session expired, please log in"),
    NOT_ENOUGH_RIGHTS("Not enough rights for this action"),
    SECTION_ALREADY_EXISTS("Section with this name already exists"),
    NOT_AUTHOR_OF_SECTION("You are not creator of this section"),
    SECTION_DOES_NOT_EXISTS("No such section on the server"),
    NOTE_ALREADY_EXISTS("Note with this subject already exists"),
    NOTE_DOES_NOT_EXISTS("No such note on the server"),
    EDIT_PARAMETERS_REQUIRED("At least one parameter is required"),
    INCORRECT_USER_IDENTIFIER("Incorrect user identifier"),
    INCORRECT_SECTION_IDENTIFIER("Incorrect section identifier"),
    INCORRECT_NOTE_IDENTIFIER("Incorrect note identifier"),
    INCORRECT_COMMENT_IDENTIFIER("Incorrect comment identifier"),
    NOT_AUTHOR_OF_NOTE("You are not creator of this note"),
    CANNOT_RATE_YOUR_OWN_NOTE("You can`t rate note that you are the author of"),
    NOT_AUTHOR_OF_COMMENT("You are not creator of this comment"),
    COMMENT_ALREADY_EXISTS("Comment with this id already exist"),
    COMMENT_DOES_NOT_EXISTS("No such comment on the server"),
    INCORRECT_RATING_FORMAT("Rating must be a number between 1 and 5"),
    INCORRECT_COUNT_FORMAT("Count must be a number"),
    INCORRECT_FROM_FORMAT("From must be a number"),
    ALREADY_FOLLOW_USER("You are already following this user"),
    ALREADY_IGNORE_USER("You are already ignoring this user");

    @Getter
    String errorString;
}
