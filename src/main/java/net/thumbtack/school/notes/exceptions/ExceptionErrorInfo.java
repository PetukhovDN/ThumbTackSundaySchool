package net.thumbtack.school.notes.exceptions;

public enum ExceptionErrorInfo {

    LOGIN_ALREADY_EXISTS("User already exists");


    private final String errorString;

    ExceptionErrorInfo(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
