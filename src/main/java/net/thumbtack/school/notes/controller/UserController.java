package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.responce.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping(value = "/api")
public class UserController {
    UserServiceImpl userService;

    @PostMapping(value = "accounts",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse registerUser(@RequestBody @Valid RegisterRequest registerRequest,
                                         HttpServletRequest request) throws NoteServerException {
        return userService.registerUser(registerRequest, request.getSession());
    }

    @PostMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@RequestBody @Valid LoginRequest loginRequest,
                          HttpServletRequest request) throws NoteServerException {
        userService.loginUser(loginRequest, request.getSession());
    }

    @DeleteMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(HttpServletRequest request) throws NoteServerException {
        HttpSession userSession = request.getSession(false);
        userService.logoutUser(userSession);
        if (!userSession.isNew()) {
            userSession.invalidate();
        }
        userSession = request.getSession(true);
    }

    @GetMapping(value = "account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getUserInfo(HttpServletRequest request) throws NoteServerException {
        return userService.getUserInfo(request.getSession(false));
    }

    @DeleteMapping(value = "accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void leaveServer(@RequestBody @Valid LeaveServerRequest leaveRequest,
                            HttpServletRequest request) throws NoteServerException {
        userService.leaveServer(leaveRequest, request.getSession(false));
    }
}

