package net.thumbtack.school.notes.dto.responce;

public class ErrorResponse {
    private String errorCode;
    private String field;
    private String message;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorCode=" + errorCode +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}