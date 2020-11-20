package net.thumbtack.school.notes.exceptions;

import lombok.Getter;
import lombok.Setter;
import net.thumbtack.school.notes.dto.responce.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(NoteServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError exceptionHandler(NoteServerException ex) {
        final MyError errors = new MyError();
        final ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getExceptionErrorInfo().toString());
        error.setField(ex.getField());
        error.setMessage(ex.getExceptionErrorInfo().getErrorString());
        errors.getErrors().add(error);
        return errors;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError exceptionHandler(MethodArgumentNotValidException exc) {
        final MyError errors = new MyError();
        exc.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ErrorResponse error = new ErrorResponse();
            error.setErrorCode(fieldError.getCode());
            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            errors.getErrors().add(error);
        });
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyError exceptionHandler(Exception ex) {
        final MyError errors = new MyError();
        final ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setField(ex.getCause().getMessage());
        error.setMessage("Global Error");
        errors.getErrors().add(error);
        return errors;
    }

    @Getter
    @Setter
    public static class MyError {
        private List<ErrorResponse> errors = new ArrayList<>();
    }
}
