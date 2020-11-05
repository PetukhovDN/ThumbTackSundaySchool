package net.thumbtack.school.notes.exceptions;

public class NoteServerException extends Exception {

    private final ExceptionErrorInfo exceptionErrorInfo;
    private final String field;

    public NoteServerException(ExceptionErrorInfo exceptionErrorInfo, String field) {
        this.exceptionErrorInfo = exceptionErrorInfo;
        this.field = field;
    }

    public ExceptionErrorInfo getExceptionErrorInfo() {
        return exceptionErrorInfo;
    }

    public String getField() {
        return field;
    }
}
