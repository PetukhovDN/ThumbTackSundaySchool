package net.thumbtack.school.notes.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.notes.dto.request.user.LeaveServerRequest;
import net.thumbtack.school.notes.dto.request.user.LoginRequest;
import net.thumbtack.school.notes.dto.request.user.RegisterRequest;
import net.thumbtack.school.notes.dto.response.user.UserInfoResponse;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.service.impl.UserServiceImpl;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
                                         HttpServletResponse response) throws NoteServerException {
        ImmutablePair<UserInfoResponse, String> resultPair = userService.registerUser(registerRequest);
        UserInfoResponse userInfoResponse = resultPair.left;
        String sessionToken = resultPair.right;
        Cookie session = new Cookie("session-token", sessionToken);

        //@Value("${user_idle_timeout}")
        //int user_idle_timeout;
        //session.setMaxAge(user_idle_timeout);
        response.addCookie(session);
        return userInfoResponse;
    }

    @PostMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@RequestBody @Valid LoginRequest loginRequest,
                          HttpServletResponse response) throws NoteServerException {
        String sessionToken = userService.loginUser(loginRequest);
        Cookie session = new Cookie("session-token", sessionToken);
        response.addCookie(session);
    }

    @DeleteMapping(value = "sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void logoutUser(@RequestHeader(name = "session-token") String sessionToken) throws NoteServerException {
        userService.logoutUser(sessionToken);
    }

    @GetMapping(value = "account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserInfoResponse getUserInfo(@RequestHeader(name = "session-token") String sessionToken) throws NoteServerException {
        return userService.getUserInfo(sessionToken);
    }

    @DeleteMapping(value = "accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void leaveServer(@RequestBody @Valid LeaveServerRequest leaveRequest,
                            @RequestHeader(name = "session-token") String sessionToken) throws NoteServerException {
        userService.leaveServer(leaveRequest, sessionToken);
    }
}

