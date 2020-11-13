package net.thumbtack.school.notes.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public @RequiredArgsConstructor
class NoteServerException extends Exception {
    @Getter
    private final ExceptionErrorInfo exceptionErrorInfo;
    @Getter
    private final String field;
}
