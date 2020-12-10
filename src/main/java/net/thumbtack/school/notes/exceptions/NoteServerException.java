package net.thumbtack.school.notes.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NoteServerException extends Exception {
    @Getter
    ExceptionErrorInfo exceptionErrorInfo;
    @Getter
    String field;
}
